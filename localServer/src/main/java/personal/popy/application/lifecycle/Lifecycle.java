package personal.popy.application.lifecycle;

public interface Lifecycle {
    void init(WebServerApplication context);

    void start(WebServerApplication context);
}
