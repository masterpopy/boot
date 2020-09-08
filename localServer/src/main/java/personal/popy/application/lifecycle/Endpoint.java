package personal.popy.application.lifecycle;

public interface Endpoint {
    <T> T getAttribute(String name, Class<T> clz);
}
