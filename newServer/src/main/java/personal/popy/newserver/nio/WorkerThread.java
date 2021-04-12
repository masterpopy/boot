package personal.popy.newserver.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.nanoTime;
import static java.util.concurrent.locks.LockSupport.park;
import static java.util.concurrent.locks.LockSupport.unpark;

public class WorkerThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);
    volatile boolean polling;
    private static final long LONGEST_DELAY = 9223372036853L;

    private static final boolean OLD_LOCKING = false;
    private static final boolean THREAD_SAFE_SELECTION_KEYS = false;
    private static final long START_TIME = System.nanoTime();

    private final Selector selector;
    private final Object workLock = new Object();

    private final Queue<Runnable> selectorWorkQueue = new ArrayDeque<>();
    private final TreeSet<TimeKey> delayWorkQueue = new TreeSet<>();

    private volatile int state;

    private static final int SHUTDOWN = (1 << 31);

    private static final AtomicIntegerFieldUpdater<WorkerThread> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(WorkerThread.class, "state");

    public WorkerThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        final Selector selector = this.selector;
        try {
            final Queue<Runnable> workQueue = selectorWorkQueue;
            final TreeSet<TimeKey> delayQueue = delayWorkQueue;
            Runnable task;
            Iterator<TimeKey> iterator;
            long delayTime = Long.MAX_VALUE;
            Set<SelectionKey> selectedKeys;
            SelectionKey[] keys = new SelectionKey[16];
            int oldState;
            int keyCount;
            for (; ; ) {
                // Run all tasks
                do {
                    synchronized (workLock) {
                        task = workQueue.poll();
                        if (task == null) {
                            iterator = delayQueue.iterator();
                            delayTime = Long.MAX_VALUE;
                            if (iterator.hasNext()) {
                                final long now = nanoTime();
                                do {
                                    final TimeKey key = iterator.next();
                                    if (key.deadline <= (now - START_TIME)) {
                                        workQueue.add(key.command);
                                        iterator.remove();
                                    } else {
                                        delayTime = key.deadline - (now - START_TIME);
                                        // the rest are in the future
                                        break;
                                    }
                                } while (iterator.hasNext());
                            }
                            task = workQueue.poll();
                        }
                    }
                    safeRun(task);
                } while (task != null);
                // all tasks have been run
                oldState = state;
                if ((oldState & SHUTDOWN) != 0) {
                    synchronized (workLock) {
                        keyCount = selector.keys().size();
                        state = keyCount | SHUTDOWN;
                        if (keyCount == 0 && workQueue.isEmpty()) {
                            // no keys or tasks left, shut down (delay tasks are discarded)
                            return;
                        }
                    }
                    synchronized (this.selector) {
                        final Set<SelectionKey> keySet = selector.keys();
                        synchronized (keySet) {
                            keys = keySet.toArray(keys);
                            Arrays.fill(keys, keySet.size(), keys.length, null);
                        }
                    }
                    // shut em down
                    for (int i = 0; i < keys.length; i++) {
                        final SelectionKey key = keys[i];
                        if (key == null) break; //end of list
                        keys[i] = null;
                        final NioPoller attachment = (NioPoller) key.attachment();
                        if (attachment != null) {
                            safeClose(key.channel());
                            attachment.destroy();
                        }
                    }
                    Arrays.fill(keys, 0, keys.length, null);
                }
                // perform select
                try {
                    if ((oldState & SHUTDOWN) != 0) {
//                        selectorLog.tracef("Beginning select on %s (shutdown in progress)", selector);
                        selector.selectNow();
                    } else if (delayTime == Long.MAX_VALUE) {
//                        selectorLog.tracef("Beginning select on %s", selector);
                        polling = true;
                        try {
                            if (workQueue.peek() != null) {
                                selector.selectNow();
                            } else {
                                selector.select();
                            }
                        } finally {
                            polling = false;
                        }
                    } else {
                        final long millis = 1L + delayTime / 1000000L;
//                        selectorLog.tracef("Beginning select on %s (with timeout)", selector);
                        polling = true;
                        try {
                            if (workQueue.peek() != null) {
                                selector.selectNow();
                            } else {
                                selector.select(millis);
                            }
                        } finally {
                            polling = false;
                        }
                    }
                } catch (CancelledKeyException ignored) {
                    // Mac and other buggy implementations sometimes spits these out
//                    selectorLog.trace("Spurious cancelled key exception");
                } catch (IOException e) {
//                    selectorLog.selectionError(e);
                    // hopefully transient; should never happen
                }
//                selectorLog.tracef("Selected on %s", selector);
                // iterate the ready key set
                synchronized (this.selector) {
                    selectedKeys = selector.selectedKeys();
                    synchronized (selectedKeys) {
                        // copy so that handlers can safely cancel keys
                        keys = selectedKeys.toArray(keys);
                        Arrays.fill(keys, selectedKeys.size(), keys.length, null);
                        selectedKeys.clear();
                    }
                }
                for (int i = 0; i < keys.length; i++) {
                    final SelectionKey key = keys[i];
                    if (key == null) break; //end of list
                    keys[i] = null;
                    final int ops;
                    try {
                        ops = key.interestOps();
                        if (ops != 0) {
//                            selectorLog.tracef("Selected key %s for %s", key, key.channel());
                            final NioPoller handle = (NioPoller) key.attachment();
                            if (handle == null) {
                                cancelKey(key);
                            } else {
//                                handle.handleReady(key.readyOps());
                            }
                        }
                    } catch (CancelledKeyException ignored) {
//                        selectorLog.tracef("Skipping selection of cancelled key %s", key);
                    } catch (Throwable t) {
//                        selectorLog.tracef(t, "Unexpected failure of selection of key %s", key);
                    }
                }
                // all selected keys invoked; loop back to run tasks
            }
        } finally {
            /*log.tracef("Shutting down channel thread \"%s\"", this);
            safeClose(selector);
            getWorker().closeResource();*/
        }
    }

    void cancelKey(final SelectionKey key) {
        assert key.selector() == selector;
        final SelectableChannel channel = key.channel();
        if (currentThread() == this) {
            try {
                key.cancel();
                try {
                    selector.selectNow();
                } catch (IOException e) {
                    logger.error("error in select", e);
                }
            } catch (Throwable t) {
                logger.info("Error cancelling key {} of {} (same thread)", key, channel);
            }
        } else if (OLD_LOCKING) {
            final SynchTask task = new SynchTask();
            queueTask(task);
            try {
                selector.wakeup();
                key.cancel();
            } catch (Throwable t) {
                logger.info("Error in cancelling key {}", key);
            } finally {
                task.done();
            }
        } else {
            try {
                key.cancel();
                selector.wakeup();
            } catch (Throwable t) {
                logger.info("Error in cancelling key {}", key);
            }
        }
    }

    void queueTask(final Runnable task) {
        synchronized (workLock) {
            selectorWorkQueue.add(task);
        }
    }

    static final AtomicLong seqGen = new AtomicLong();

    final class TimeKey implements Key, Comparable<TimeKey> {
        private final long deadline;
        private final long seq = seqGen.incrementAndGet();
        private final Runnable command;

        TimeKey(final long deadline, final Runnable command) {
            this.deadline = deadline;
            this.command = command;
        }

        public boolean remove() {
            synchronized (workLock) {
                return delayWorkQueue.remove(this);
            }
        }

        public int compareTo(final TimeKey o) {
            int r = Long.signum(deadline - o.deadline);
            if (r == 0) r = Long.signum(seq - o.seq);
            return r;
        }
    }

    final class SynchTask implements Runnable {
        volatile boolean done;

        public void run() {
            while (!done) {
                park();
            }
        }

        void done() {
            done = true;
            unpark(WorkerThread.this);
        }
    }

    private static void safeRun(final Runnable command) {
        if (command != null) try {
//            log.tracef("Running task %s", command);
            command.run();
        } catch (Throwable t) {
            logger.trace("Error in run task: {}", command);
        }
    }

    public static void safeClose(final Selector resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (ClosedChannelException ignored) {
        } catch (Throwable t) {
            logger.trace("Error in close Selector {}", resource);
        }
    }

    public static void safeClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (ClosedChannelException ignored) {
        } catch (Throwable t) {
            logger.trace("Error in close resource {}", resource);
        }
    }

    public void execute(final Runnable command) {
        if ((state & SHUTDOWN) != 0) {
            throw new IllegalStateException();
        }
        synchronized (workLock) {
            selectorWorkQueue.add(command);
        }
        if (polling) { // flag is always false if we're the same thread
            selector.wakeup();
        }
    }

    public Key executeAfter(final Runnable command, final long time, final TimeUnit unit) {
        final long millis = unit.toMillis(time);
        if ((state & SHUTDOWN) != 0) {
            throw new IllegalStateException();
        }
        if (millis <= 0) {
            execute(command);
            return Key.IMMEDIATE;
        }
        final long deadline = (nanoTime() - START_TIME) + Math.min(millis, LONGEST_DELAY) * 1000000L;
        final TimeKey key = new TimeKey(deadline, command);
        synchronized (workLock) {
            final TreeSet<TimeKey> queue = delayWorkQueue;
            queue.add(key);
            if (queue.iterator().next() == key) {
                // we're the next one up; poke the selector to update its delay time
                if (polling) { // flag is always false if we're the same thread
                    selector.wakeup();
                }
            }
            return key;
        }
    }

    class RepeatKey implements Key, Runnable {
        private final Runnable command;
        private final long millis;
        private final AtomicReference<Key> current = new AtomicReference<>();

        RepeatKey(final Runnable command, final long millis) {
            this.command = command;
            this.millis = millis;
        }

        public boolean remove() {
            final Key removed = current.getAndSet(this);
            // removed key should not be null because remove cannot be called before it is populated.
            assert removed != null;
            return removed != this && removed.remove();
        }

        void setFirst(Key key) {
            current.compareAndSet(null, key);
        }

        public void run() {
            try {
                command.run();
            } finally {
                Key o, n;
                o = current.get();
                if (o != this) {
                    n = executeAfter(this, millis, TimeUnit.MILLISECONDS);
                    if (!current.compareAndSet(o, n)) {
                        n.remove();
                    }
                }
            }
        }
    }

    public Key executeAtInterval(final Runnable command, final long time, final TimeUnit unit) {
        final long millis = unit.toMillis(time);
        final RepeatKey repeatKey = new RepeatKey(command, millis);
        final Key firstKey = executeAfter(repeatKey, millis, TimeUnit.MILLISECONDS);
        repeatKey.setFirst(firstKey);
        return repeatKey;
    }

    SelectionKey registerChannel(final AbstractSelectableChannel channel) throws ClosedChannelException {
        if (currentThread() == this) {
            return channel.register(selector, 0);
        } else if (THREAD_SAFE_SELECTION_KEYS) {
            try {
                return channel.register(selector, 0);
            } finally {
                if (polling) selector.wakeup();
            }
        } else {
            final SynchTask task = new SynchTask();
            queueTask(task);
            try {
                // Prevent selector from sleeping until we're done!
                selector.wakeup();
                return channel.register(selector, 0);
            } finally {
                task.done();
            }
        }
    }

    void shutdown() {
        int oldState;
        do {
            oldState = state;
            if ((oldState & SHUTDOWN) != 0) {
                // idempotent
                return;
            }
        } while (!stateUpdater.compareAndSet(this, oldState, oldState | SHUTDOWN));
        if (currentThread() != this) {
            selector.wakeup();
        }
    }

    void setOps(final SelectionKey key, final int ops) {
        if (currentThread() == this) {
            try {
                key.interestOps(key.interestOps() | ops);
            } catch (CancelledKeyException ignored) {
            }
        } else if (OLD_LOCKING) {
            final SynchTask task = new SynchTask();
            queueTask(task);
            try {
                // Prevent selector from sleeping until we're done!
                selector.wakeup();
                key.interestOps(key.interestOps() | ops);
            } catch (CancelledKeyException ignored) {
            } finally {
                task.done();
            }
        } else {
            try {
                key.interestOps(key.interestOps() | ops);
                if (polling) selector.wakeup();
            } catch (CancelledKeyException ignored) {
            }
        }
    }

    void clearOps(final SelectionKey key, final int ops) {
        if (currentThread() == this || !OLD_LOCKING) {
            try {
                key.interestOps(key.interestOps() & ~ops);
            } catch (CancelledKeyException ignored) {
            }
        } else {
            final SynchTask task = new SynchTask();
            queueTask(task);
            try {
                // Prevent selector from sleeping until we're done!
                selector.wakeup();
                key.interestOps(key.interestOps() & ~ops);
            } catch (CancelledKeyException ignored) {
            } finally {
                task.done();
            }
        }
    }

}
