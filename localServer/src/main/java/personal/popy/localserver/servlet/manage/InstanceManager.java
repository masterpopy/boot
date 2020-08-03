package personal.popy.localserver.servlet.manage;

import personal.popy.localserver.action.Setter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface InstanceManager<T> extends Supplier<T> {

    static <S> InstanceManager<S> identity(S s) {
        return () -> s;
    }

    static <S> InstanceManager<S> lazy(Setter<InstanceManager<S>> c, InstanceManager<S> i) {
        return () -> {
            S t = i.get();
            c.set(identity(t));
            return t;
        };
    }

    static <S> InstanceManager<S> postConstruct(InstanceManager<S> i, Consumer<S> c) {
        return () -> {
            S s = i.get();
            c.accept(s);
            return s;
        };
    }
}
