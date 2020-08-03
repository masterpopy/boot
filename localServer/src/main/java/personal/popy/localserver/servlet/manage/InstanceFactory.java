package personal.popy.localserver.servlet.manage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceFactory {
    private final ClassLoader loader;

    public InstanceFactory(ClassLoader loader) {
        this.loader = loader;
    }

    public <T> InstanceManager<T> newInstance(String className) {
        try {
            return newInstance((Class<T>) loader.loadClass(className));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("class not found");
        }
    }

    public <T> InstanceManager<T> newInstance(Class<T> clz) {
        return () -> {
            try {
                Constructor<T> constructor = clz.getConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new IllegalArgumentException("create instance fail");
            }
        };

    }

    public <T> InstanceManager<T> newInstance(T obj) {
        return InstanceManager.identity(obj);
    }

    public ClassLoader getClassLoader() {
        return loader;
    }
}
