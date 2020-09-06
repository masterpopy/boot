package personal.popy.application.lifecycle;

import personal.popy.server.factory.ClassResourceLoader;
import personal.popy.server.factory.WebResourceLoader;
import personal.popy.server.lifecycle.ConnectionContext;

public class WebServerApplication implements ContainerFactory{

    private String proxyName = "localhost";

    private ConnectionContext connectionContext = new ConnectionContext();

    private Lifecycle initializer;

    private WebServerContainer[] baseServerContainers;

    private WebResourceLoader webResourceLoader = new ClassResourceLoader();

    private void init() {

    }


    public void start() throws Exception {
        initializer.init(this);
        connectionContext.setServer(this);
        connectionContext.init();
        initializer.start(this);
    }

    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }


    public void stop() {
        connectionContext.destroy();
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public void setInitializer(Lifecycle initializer) {
        this.initializer = initializer;
    }

    @Override
    public <T extends Container> T create(Class<T> clz) {
        return null;
    }
}
