package personal.popy.localserver.lifecycle;

public interface Lifecycle {
    void init(WebServerApplication context);

    void start(WebServerApplication context);
}
