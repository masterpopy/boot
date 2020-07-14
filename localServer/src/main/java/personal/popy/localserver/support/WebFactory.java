package personal.popy.localserver.support;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import personal.popy.localserver.lifecycle.ConnectionContext;
import personal.popy.localserver.lifecycle.Http11ConnHandler;
import personal.popy.localserver.lifecycle.ServerContext;

import javax.servlet.ServletException;

public class WebFactory implements ServletWebServerFactory, WebServer {
    private ServerContext container;
    private Http11ConnHandler processor;

    public WebFactory() {
        container = new ServerContext();
        ConnectionContext connectionContext = new ConnectionContext();
        processor = new Http11ConnHandler();
        container.setConnectionContext(connectionContext);
        container.setProcessor(processor);
    }

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        for (ServletContextInitializer initializer : initializers) {
            try {
                initializer.onStartup(processor.getServletContext());
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        //no use
        processor = null;
        return this;
    }

    @Override
    public void start() throws WebServerException {
        try {
            container.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws WebServerException {
        container.stop();
    }

    @Override
    public int getPort() {
        return container.getConnectionContext().getPort();
    }

}
