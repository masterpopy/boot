package personal.popy.localserver.lifecycle;

import personal.popy.localserver.factory.ClassResourceLoader;
import personal.popy.localserver.factory.WebResourceLoader;

public class WebServerApplication {

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
        connectionContext.destory();
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
}
