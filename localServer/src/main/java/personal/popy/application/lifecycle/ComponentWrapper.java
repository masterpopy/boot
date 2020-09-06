package personal.popy.application.lifecycle;

public interface ComponentWrapper {
    <T> T getComponent(ComponentDefinition<T> c);
}
