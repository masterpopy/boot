package personal.popy.server.factory;

import personal.popy.application.lifecycle.Lifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AnnotationProvider {
    Class<? extends Lifecycle> value();
}
