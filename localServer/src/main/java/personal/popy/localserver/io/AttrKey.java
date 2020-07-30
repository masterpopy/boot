package personal.popy.localserver.io;

public final class AttrKey<T> {
    private final Class<T> cl;

    private AttrKey(Class<T> cl) {
        this.cl = cl;
    }

    public T cast(Object o) {
        return cl.cast(o);
    }

    public static <T> AttrKey<T> make(Class<T> cl) {
        return new AttrKey<>(cl);
    }
}
