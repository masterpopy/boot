package personal.popy.server.support;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import personal.popy.application.lifecycle.WebServerApplication;

import javax.servlet.ServletException;

public class WebFactory implements ServletWebServerFactory, WebServer {
    private WebServerApplication container;

    private ServletInitializer servletInitializer = new ServletInitializer();
    public WebFactory() {
        container = new WebServerApplication();
        container.setInitializer(servletInitializer);
    }

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        for (ServletContextInitializer initializer : initializers) {
            try {
                initializer.onStartup(servletInitializer.getHandler().getServletContext());
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        //no use
        servletInitializer = null;
        return this;
    }

    @Override
    public void start() throws WebServerException {
        try {
            container.start();
        } catch (Exception e) {
            throw new WebServerException(e.getMessage(), e);
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
