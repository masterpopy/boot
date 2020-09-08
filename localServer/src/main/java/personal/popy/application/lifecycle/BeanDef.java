package personal.popy.application.lifecycle;

public interface BeanDef<T> {
    String name();
    Class<T> tClass();
}
