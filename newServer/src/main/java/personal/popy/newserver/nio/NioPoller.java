package personal.popy.newserver.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicInteger;

public class NioPoller implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NioPoller.class);
    private Selector selector;
    private final ArrayDeque<NioChannel> events =
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
        if (selector == null)
            throw new NullPointerException();
        this.selector = selector;
    }

    protected void destroy() {
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

    public void reg(NioChannel event) {
        if (event == null)
            throw new NullPointerException();
        synchronized (events) {
            events.offer(event);
        }
        if (wakeupCounter.incrementAndGet() == 0) {
            selector.wakeup();
        }
    }

    public void ops(NioChannel event) {
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
            for (NioChannel event : events) {
                try {
                    event.ch.register(selector, SelectionKey.OP_READ, event);
                } catch (Throwable e) {
                    logger.error("error in register", e);
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
                        logger.error("error in selector close", ioe);
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

    private void unReg(SelectionKey key, NioChannel events) {
        try {
            key.interestOps(key.interestOps() & (~key.readyOps()));
        } catch (CancelledKeyException e) {
            logger.trace("Error in key interest: {}", key);
            cancelledKey(key);
        }
        events.ops = key.interestOps();
    }

    private void processKey(SelectionKey key) {
        Object attachment = key.attachment();
        if (attachment instanceof NioChannel) {
            unReg(key, (NioChannel) attachment);
            try {
                ((NioChannel) attachment).process(key.readyOps());
            } catch (Throwable e) {
                logger.trace("Error in process key: {}", attachment);
                cancelledKey(key);
            }
        }
    }

    private void timeout(int keyCount, boolean hasEvents) {
        long now = System.currentTimeMillis();

        if (nextExpiration > 0 && (keyCount > 0 || hasEvents) && (now < nextExpiration) && !close) {
            return;
        }

        try {
            for (SelectionKey key : selector.keys()) {
                try {
                    NioChannel event = (NioChannel) key.attachment();
                    if (event == null) {
                        cancelledKey(key);
                    } else if (close) {
                        cancelledKey(key);
                    } else if ((event.ops & SelectionKey.OP_READ) == SelectionKey.OP_READ ||
                            (event.ops & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
                        boolean readTimeout = false;
                        boolean writeTimeout = false;
                        if ((event.ops & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                            long timeout = event.readTimeout;
                            if (timeout > 0 && now - event.lastRead > timeout) {
                                readTimeout = true;
                            }
                        }
                        if (!readTimeout && (event.ops & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
                            long timeout = event.writeTimeout;
                            if (timeout > 0 && now - event.lastWrite > timeout) {
                                writeTimeout = true;
                            }
                        }
                        if (readTimeout || writeTimeout) {
                            key.interestOps(0);
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
            cme.printStackTrace();
        }
        nextExpiration = System.currentTimeMillis() + timeoutInterval;
    }

    private void cancelledKey(SelectionKey key) {
        try {
            if (key != null) {
                NioChannel attachment = (NioChannel) key.attach(null);
                if (attachment != null) {
                    attachment.ops = 0;
                    attachment.cancelled();
                }
                if (key.isValid()) {
                    key.cancel();
                }
            }
        } catch (Throwable e) {
            logger.trace("Error in cancelling key: {}", key);
        }
    }

}
