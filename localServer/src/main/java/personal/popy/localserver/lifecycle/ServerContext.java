package personal.popy.localserver.lifecycle;

public class ServerContext {

    private String proxyName = "localhost";

    private ConnectionContext connectionContext = new ConnectionContext();

    private ServerInitializer initializer;

    public void start() throws Exception {
        initializer.onInitialzing(this);
        connectionContext.setServer(this);
        connectionContext.start();
        initializer.onInitialized(this);
    }

    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }


    public void stop() {
        connectionContext.stop();
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public void setInitializer(ServerInitializer initializer) {
        this.initializer = initializer;
    }
}
