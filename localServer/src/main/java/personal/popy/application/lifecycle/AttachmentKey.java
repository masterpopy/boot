package personal.popy.application.lifecycle;

import java.util.concurrent.atomic.AtomicInteger;

public class AttachmentKey<T> {
    private final Class<T> valueClass;
    private final int index;

    private static final AtomicInteger counts = new AtomicInteger();

    protected AttachmentKey(Class<T> valueClass) {
        this.valueClass = valueClass;
        index = counts.getAndIncrement();
    }

    public T cast(Object o) {
        return valueClass.cast(o);
    }

    public int getIndex() {
        return index;
    }
}
