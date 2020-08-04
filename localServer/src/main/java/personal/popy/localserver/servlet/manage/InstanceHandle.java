package personal.popy.localserver.servlet.manage;

import java.util.Optional;

public class InstanceHandle<T> implements InstanceHandler {
    private T obj;

    public InstanceHandle(T obj) {
        this.obj = obj;
    }
    @Override
    public T get() {
        return obj;
    }


}
