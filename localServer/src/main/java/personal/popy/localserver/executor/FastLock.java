package personal.popy.localserver.executor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class FastLock {
    private AtomicInteger state = new AtomicInteger();

    private AtomicReferenceArray<Thread> waiters = new AtomicReferenceArray<>(64);

    private AtomicInteger head = new AtomicInteger();//这个变量是线程安全的

    private AtomicInteger tail = new AtomicInteger();

    private volatile Thread owner;

    ReentrantLock lock;

    public void lock() {
        Thread thread = Thread.currentThread();
        if (owner == thread) {
            state.incrementAndGet();
            return;
        }
        int t = tail.getAndIncrement();//原子操作
        int i = t & (waiters.length() - 1);
        waiters.set(i, thread);

        if (head.get() != t || !state.compareAndSet(0, -1)) {
            LockSupport.park();
        }


        head.getAndAdd(1);
        state.set(1);
        waiters.set(i, null);
        owner = thread;
    }


    public void unlock() {
        Thread thread = Thread.currentThread();
        if (owner == thread) {
            if (state.getAndDecrement() == 1) {
                owner = null;
                int h = head.get();

                int t = tail.get();
                if (!state.compareAndSet(0, -1)) {
                    return;
                }

                if (h <= t) {
                    Thread th;
                    while ((th = waiters.get(h & (waiters.length() - 1))) == null) {
                        Thread.yield();
                    }
                    LockSupport.unpark(th);
                }
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }
}
