package personal.popy.localserver.lifecycle;

import java.util.HashMap;

public class ServerContext {

    private String proxyName="localhost";

    private ConnectionContext connectionContext;

    private ConnHandler processor;

    private HashMap<String, String> properties;

    public String getProperties(String value) {
        String ret = properties.get(value);
        if (ret == null) {
            return System.getProperty(value);
        }
        return ret;
    }

    public void setProperties(String prop, String value) {
        properties.put(prop, value);
    }

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
        this.proxyName  = proxyName;
    }

    public void setProcessor(ConnHandler processor) {
        this.processor = processor;
    }
}
