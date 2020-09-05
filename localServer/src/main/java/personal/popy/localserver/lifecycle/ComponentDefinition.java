package personal.popy.localserver.lifecycle;

public interface ComponentDefinition<T> {
    String name();
    Class<T> tClass();
    T target();
}
