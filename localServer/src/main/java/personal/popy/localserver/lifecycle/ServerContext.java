package personal.popy.localserver.lifecycle;

public class ServerContext {

    private String proxyName = "localhost";

    private ConnectionContext connectionContext;

    private ConnHandler processor;

    public ConnHandler getProcessor() {
        return processor;
    }

    public void start() throws Exception {
        connectionContext.setServer(this);
        processor.setServer(this);
        connectionContext.start();
    }

    public void setConnectionContext(ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
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

    public void setProcessor(ConnHandler processor) {
        this.processor = processor;
    }
}
