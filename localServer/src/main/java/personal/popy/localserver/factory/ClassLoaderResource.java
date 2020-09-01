package personal.popy.localserver.factory;

import personal.popy.localserver.exception.UnHandledException;
import personal.popy.localserver.factory.io.Resource;
import personal.popy.localserver.factory.io.support.PathMatchingResourcePatternResolver;
import personal.popy.localserver.factory.io.support.ResourcePatternResolver;
import personal.popy.localserver.util.ClassUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

//嵌入式版可以直接使用classloader来加载
public class ClassLoaderResource implements WebResource {

    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public ClassLoaderResource() {

    }

    @Override
    public List<URL> list(String path) {
        try {
            Enumeration<URL> resources = resolver.getClassLoader().getResources(path);
            return Collections.list(resources);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Class<?> load(String className) {
        try {
            return resolver.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Resource[] find(String basePackage) {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(basePackage) + '/' + ResourcePatternResolver.DEFAULT_RESOURCE_PATTERN;
        try {
            return resolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new UnHandledException(e);
        }
    }
}
