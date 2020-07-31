package personal.popy.localserver.servlet.manage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceFactory {
    private ClassLoader loader;

    public InstanceFactory(ClassLoader loader) {
        this.loader = loader;
    }

    public <T> InstanceHandler<T> newInstance(String className) {
        try {
            return newInstance((Class<T>) loader.loadClass(className));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("class not found");
        }
    }

    public <T> InstanceHandler<T> newInstance(Class<T> clz) {
        return ()->{
            try {
                Constructor<T> constructor = clz.getConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new IllegalArgumentException("create instance fail");
            }
        };

    }

    public <T> InstanceHandler<T> newInstance(T obj) {
        return InstanceHandler.identity(obj);
    }

}
