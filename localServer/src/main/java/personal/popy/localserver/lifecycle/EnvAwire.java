package personal.popy.localserver.lifecycle;

public class EnvAwire {
    protected ServerContext server;

    public void setServer(ServerContext server) {
        this.server = server;
    }

    public ServerContext getServer() {
        return server;
    }
}
