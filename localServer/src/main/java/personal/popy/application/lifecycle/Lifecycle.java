package personal.popy.application.lifecycle;

import personal.popy.application.lifecycle.WebServerApplication;

public interface Lifecycle {
    void init(WebServerApplication context);

    void start(WebServerApplication context);
}
