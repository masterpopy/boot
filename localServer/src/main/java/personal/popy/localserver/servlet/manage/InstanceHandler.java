package personal.popy.localserver.servlet.manage;

import java.util.function.Supplier;

public interface InstanceHandler<T> extends Supplier<T> {

    static <S> InstanceHandler<S> identity(S s) {
        return () -> s;
    }
}
