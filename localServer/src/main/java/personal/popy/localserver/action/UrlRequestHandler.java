package personal.popy.localserver.action;

import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.servlet.manage.InstanceHandler;
import personal.popy.localserver.servlet.registry.ServletConfigImpl;
import personal.popy.localserver.servlet.registry.ServletRegistrationDynamicImpl;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

public class UrlRequestHandler implements RequestHandler {

    private InstanceHandler<? extends Servlet> servlet;

    public UrlRequestHandler() {
    }

    public UrlRequestHandler(ServletRegistrationDynamicImpl reg) {
        InstanceHandler<? extends Servlet> s = reg.getServlet();
        ServletConfigImpl servletConfig = reg.getServletConfig();
        s = InstanceHandler.postConstruct(s, servletConfig::initServlet);
        if (reg.getLoadOnStartup() < 0) {
            this.servlet = InstanceHandler.lazy((v) -> this.servlet = v, s);
        } else {
            servlet = s;
        }
    }

    @Override
    public void doReq(HttpExchanger exchanger) {
        try {
            servlet.get().service(exchanger.getRequest(), exchanger.createResponse());
            exchanger.createResponse().doResponse();
        } catch (ServletException | IOException e) {
            exchanger.createResponse().setStatus(300);
        }
    }

}
