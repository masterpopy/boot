package personal.popy.localserver.factory;

import personal.popy.copy.spring.io.Resource;
import personal.popy.copy.spring.io.support.PathMatchingResourcePatternResolver;
import personal.popy.copy.spring.io.support.ResourcePatternResolver;
import personal.popy.copy.spring.util.ClassUtils;
import personal.popy.localserver.exception.UnHandledException;

import java.io.IOException;

//嵌入式版可以直接使用classloader来加载
public class ClassResourceLoader implements WebResourceLoader {

    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public ClassResourceLoader() {

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
