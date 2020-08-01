package personal.popy.localserver.servlet.manage;

import personal.popy.localserver.action.Setter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface InstanceHandler<T> extends Supplier<T> {

    static <S> InstanceHandler<S> identity(S s) {
        return () -> s;
    }

    static <S> InstanceHandler<S> lazy(Setter<InstanceHandler<S>> c, InstanceHandler<S> i) {
        return () -> {
            S t = i.get();
            c.set(identity(t));
            return t;
        };
    }

    static <S> InstanceHandler<S> postConstruct(InstanceHandler<S> i, Consumer<S> c) {
        return () -> {
            S s = i.get();
            c.accept(s);
            return s;
        };
    }
}
