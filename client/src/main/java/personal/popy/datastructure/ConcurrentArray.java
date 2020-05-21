package personal.popy.datastructure;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentArray<T> {
    public volatile Object[] elements;
    public final AtomicInteger index = new AtomicInteger();
    final AtomicInteger lock = new AtomicInteger();
    public static final int EXPAND_LOCK = 1;

    public ConcurrentArray(){
        elements = new Object[4];
    }

    public boolean add(T t) {
        int i = index.getAndIncrement();
        if (i == Integer.MAX_VALUE)
            return false;
        Object[] e = elements;
        while (e.length <= i) {
            if (lock.compareAndSet(0, EXPAND_LOCK)) {
                try {
                    e = elements;
                    if (e.length < i)
                    elements = Arrays.copyOf(e, Math.max(e.length * 2, max2(index.get())));
                } finally {
                    lock.set(0);
                }
            } else {
                Thread.yield();
                e = elements;
            }
        }
        e[i] = t;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T get(int index){
        return (T) elements[index];
    }

    static int max2(int v){
        int r = 2;
        while (v > r){
            r <<= 1;
        }
        return r;
    }

    @Override
    public String toString() {
        Object[] e = elements;
        if(e.length > 1024){
            e = Arrays.copyOf(e, 1024);
        }
        return Arrays.toString(e);
    }
}
