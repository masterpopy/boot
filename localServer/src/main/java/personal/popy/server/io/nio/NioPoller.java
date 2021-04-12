package personal.popy.server.io.nio;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NioPoller implements Runnable {

    private Selector selector;
    private final ArrayDeque<PollerEvent> events =
            new ArrayDeque<>(32);

    private volatile boolean close = false;
    // Optimize expiration handling
    private long nextExpiration = 0;

    private AtomicInteger wakeupCounter = new AtomicInteger();



    private int selectorTimeout = 1000;

    /**
     * The minimum frequency of the timeout interval to avoid excess load from
     * the poller during high traffic
     */
    private int timeoutInterval = 1000;

    public NioPoller(Selector selector) {
        this.selector = selector;
    }

    /**
     * Destroy the poller.
     */
    protected void destroy() {
        // Wait for polltime before doing anything, so that the poller threads
        // exit, otherwise parallel closure of sockets which are still
        // in the poller can cause problems
        close = true;
        selector.wakeup();
    }

    public void setSelectorTimeout(int selectorTimeout) {
        if (selectorTimeout < 1000)
            throw new IllegalArgumentException();
        this.selectorTimeout = selectorTimeout;
    }

    public void setTimeoutInterval(int timeoutInterval) {
        if (timeoutInterval < 1000)
            throw new IllegalArgumentException();
        this.timeoutInterval = timeoutInterval;
    }

    public void reg(PollerEvent event) {
        if (event == null)
            throw new NullPointerException();
        synchronized (events) {
            events.offer(event);
        }
        if (wakeupCounter.incrementAndGet() == 0) {
            selector.wakeup();
        }
    }

    public void ops(PollerEvent event) {
        SelectionKey key = event.ch.keyFor(selector);
        try {
            if (key != null)
                key.interestOps(event.ops);
        } catch (CancelledKeyException ignored) {
            cancelledKey(key);
            return;
        }
        if (wakeupCounter.incrementAndGet() == 0) {
            selector.wakeup();
        }
    }

    private boolean processReg() {
        if (events.size() == 0)
            return false;
        synchronized (events) {
            for (PollerEvent event : events) {
                try {
                    event.ch.register(selector, SelectionKey.OP_READ, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (true) {

            boolean hasEvents = false;
            int keyCount = 0;
            try {
                if (!close) {
                    hasEvents = processReg();
                    if (wakeupCounter.getAndSet(-1) > 0) {
                        // If we are here, means we have other stuff to do
                        // Do a non blocking select
                        keyCount = selector.selectNow();
                    } else {
                        keyCount = selector.select(selectorTimeout);
                    }
                    wakeupCounter.set(0);
                }
                if (close) {
                    processReg();
                    timeout(0, false);
                    try {
                        selector.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                }
            } catch (Throwable x) {

                x.printStackTrace();
                continue;
            }
            // Either we timed out or we woke up, process events first
            if (keyCount == 0) {
                hasEvents = (hasEvents | processReg());
            } else {
                for (SelectionKey selectedKey : selector.selectedKeys()) {
                    processKey(selectedKey);
                }
                selector.selectedKeys().clear();
            }
            // Process timeouts
            timeout(keyCount, hasEvents);
        }
    }

    private void unreg(SelectionKey key, PollerEvent events) {
        try {
            key.interestOps(key.interestOps() & (~key.readyOps()));
        } catch (CancelledKeyException e) {
            e.printStackTrace();
            cancelledKey(key);
        }
        events.ops = key.interestOps();
    }

    private void processKey(SelectionKey key) {
        Object attachment = key.attachment();
        if (attachment instanceof PollerEvent) {
            unreg(key, (PollerEvent) attachment);
            try {
                ((PollerEvent) attachment).process(key.readyOps());
            } catch (Throwable e) {
                e.printStackTrace();
                cancelledKey(key);
            }
        }
    }

    private void timeout(int keyCount, boolean hasEvents) {
        long now = System.currentTimeMillis();
        // This method is called on every loop of the Poller. Don't process
        // timeouts on every loop of the Poller since that would create too
        // much load and timeouts can afford to wait a few seconds.
        // However, do process timeouts if any of the following are true:
        // - the selector simply timed out (suggests there isn't much load)
        // - the nextExpiration time has passed
        // - the server socket is being closed
        if (nextExpiration > 0 && (keyCount > 0 || hasEvents) && (now < nextExpiration) && !close) {
            return;
        }

        try {
            for (SelectionKey key : selector.keys()) {
                try {
                    PollerEvent event = (PollerEvent) key.attachment();
                    if (event == null) {
                        // We don't support any keys without attachments
                        cancelledKey(key);
                    } else if (close) {
                        cancelledKey(key);
                    } else if ((event.ops & SelectionKey.OP_READ) == SelectionKey.OP_READ ||
                            (event.ops & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
                        boolean readTimeout = false;
                        boolean writeTimeout = false;
                        // Check for read timeout
                        if ((event.ops & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                            long timeout = event.readTimeout;
                            if (timeout > 0 && now - event.lastRead > timeout) {
                                readTimeout = true;
                            }
                        }
                        // Check for write timeout
                        if (!readTimeout && (event.ops & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
                            long timeout = event.writeTimeout;
                            if (timeout > 0 && now - event.lastWrite > timeout) {
                                writeTimeout = true;
                            }
                        }
                        if (readTimeout || writeTimeout) {
                            key.interestOps(0);
                            // Avoid duplicate timeout calls
                            event.ops = 0;
                            if (readTimeout && !event.processReadTimeout()) {
                                cancelledKey(key);
                            } else if (writeTimeout && !event.processWriteTimeout()) {
                                cancelledKey(key);
                            }
                        }
                    }
                } catch (CancelledKeyException ckx) {
                    cancelledKey(key);
                }
            }
        } catch (ConcurrentModificationException cme) {
            // See https://bz.apache.org/bugzilla/show_bug.cgi?id=57943
            cme.printStackTrace();
        }
        // For logging purposes only
        nextExpiration = System.currentTimeMillis() + timeoutInterval;
    }

    private void cancelledKey(SelectionKey key) {
        try {
            // If is important to cancel the key first, otherwise a deadlock may occur between the
            // poller select and the socket channel close which would cancel the key
            if (key != null) {
                PollerEvent attachment = (PollerEvent) key.attach(null);
                if (attachment != null) {
                    attachment.ops = 0;
                    attachment.cancelled();
                }
                if (key.isValid()) {
                    key.cancel();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
