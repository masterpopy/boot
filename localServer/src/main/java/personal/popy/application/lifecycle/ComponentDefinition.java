package personal.popy.application.lifecycle;

public interface ComponentDefinition<T> {
    String name();
    Class<T> tClass();
    T target();
}
