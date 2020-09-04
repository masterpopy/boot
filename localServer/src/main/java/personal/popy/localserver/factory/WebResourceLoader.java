package personal.popy.localserver.factory;

import personal.popy.copy.spring.io.Resource;

public interface WebResourceLoader {
    static ClassLoader getDefaultLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }

        if (cl == null) {
            cl = WebResourceLoader.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {
                }
            }
        }

        return cl;
    }

    Resource[] find(String basePackage);

    Class<?> load(String className);
}
