package personal.popy.localserver.lifecycle;

public class EnvAwire {
    protected WebServerApplication server;

    public void setServer(WebServerApplication server) {
        this.server = server;
    }

    public WebServerApplication getServer() {
        return server;
    }
}
