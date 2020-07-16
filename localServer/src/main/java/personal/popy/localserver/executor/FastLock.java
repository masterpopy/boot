package personal.popy.localserver.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;

public class FastLock {

    private AtomicReferenceArray<Thread> threads = new AtomicReferenceArray<>(32);

    private AtomicInteger state = new AtomicInteger();


    private AtomicInteger pos = new AtomicInteger(-1);



    public void lock() {
        Thread thread = Thread.currentThread();

        while (true) {
            if (state.compareAndSet(0, 1)) {//increase;
                int i = pos.getAndIncrement();
                if (i == -1) {
                    state.set(0);
                } else {
                    threads.set(i, thread);
                    state.set(0);
                    LockSupport.park();
                    if (state.get() == 0) {//可能已经被释放。
                        continue;
                    }
                    state.set(0);
                }
                return;
            } else {
                Thread.yield();
            }
        }

    }

    public void unlock() {
        while (true) {
            if (state.compareAndSet(0, 1)) {

                int i = pos.decrementAndGet();
                if (i >= 0) {
                    Thread andSet = threads.getAndSet(i, null);
                    if (andSet == null) {
                        throw new NullPointerException("null thread");
                    }
                    LockSupport.unpark(andSet);
                } else {
                    state.set(0);
                }

                return;
            } else {
                Thread.yield();
            }
        }
    }

    public List<Thread> getThreads() {
        ArrayList<Thread> list = new ArrayList<>(32);
        for (int i = 0; i < 32; i++) {
            if (threads.get(i) != null) {
                list.add(threads.get(i));
            }
        }
        return list;
    }
}
