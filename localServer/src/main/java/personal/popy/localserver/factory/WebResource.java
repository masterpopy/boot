package personal.popy.localserver.factory;

import java.net.URL;
import java.util.List;

public interface WebResource {
    static ClassLoader getDefaultLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }

        if (cl == null) {
            cl = WebResource.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {
                }
            }
        }

        return cl;
    }

    List<URL> list(String path);
    Class<?> load(String className);
}
