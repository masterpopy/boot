package personal.popy.localserver.executor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;

public class FastLock {

    private static final int LENGTH = 64;


    private AtomicInteger state = new AtomicInteger();

    private AtomicReferenceArray<Thread> waiters = new AtomicReferenceArray<>(LENGTH);

    private volatile Thread owner;


    public void lock() {
        Thread thread = Thread.currentThread();

        while (true) {
            int i = state.get();
            if (state.compareAndSet(i, i + 1)) {
                if (i == 0) {
                    owner = thread;
                } else {
                    while (waiters.get(i) != null) {
                        System.out.println("full");
                        Thread.yield();
                    }
                    waiters.set(i, thread);
                    LockSupport.park();
                    waiters.set(i, null);
                    Thread owner = this.owner;
                    if (owner != null) {
                        System.out.println(1);
                    }
                    this.owner = thread;
                }
                return;
            }
        }

    }


    public void unlock() {
        Thread thread = Thread.currentThread();
        Thread owner = this.owner;
        if (owner == thread) {
            this.owner = null;
            while (true) {
                int i = state.get();
                if (state.compareAndSet(i, i - 1)) {
                    if (i > 1) {
                        --i;
                        Thread th;
                        while ((th = waiters.get(i)) == null){
                            System.out.println("null");
                            Thread.yield();
                        }
                        LockSupport.unpark(th);
                    }
                    return;
                }
            }

        } else {
            throw new IllegalMonitorStateException(thread.getName() + ":" + owner);
        }
    }
}
