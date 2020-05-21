package personal.popy.datastructure;

import java.util.concurrent.atomic.AtomicInteger;

public class RingArray<T> extends ConcurrentArray<T> {
    public final AtomicInteger start = new AtomicInteger();

    @Override
    public boolean add(T t) {
        int i = index.getAndIncrement();
        if (i == Integer.MAX_VALUE)
            return false;
        Object[] e = elements;
        while ((i & (e.length - 1)) >= start.get()){

        }
        return true;
    }
}
