package personal.popy.server.lifecycle;

import personal.popy.application.lifecycle.WebServerApplication;

public class EnvAwire {
    protected WebServerApplication server;

    public void setServer(WebServerApplication server) {
        this.server = server;
    }

    public WebServerApplication getServer() {
        return server;
    }
}
