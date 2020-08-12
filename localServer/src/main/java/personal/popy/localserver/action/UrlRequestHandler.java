package personal.popy.localserver.action;

import personal.popy.localserver.servlet.RequestImpl;
import personal.popy.localserver.servlet.ResponseImpl;
import personal.popy.localserver.servlet.manage.InstanceManager;
import personal.popy.localserver.servlet.registry.ServletConfigImpl;
import personal.popy.localserver.servlet.registry.ServletRegistrationDynamicImpl;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.MappingMatch;
import java.io.IOException;

public class UrlRequestHandler implements RequestHandler {

    private InstanceManager<? extends Servlet> servlet;
    private MappingMatch matching;
    private String pattern;

    public UrlRequestHandler(InstanceManager<? extends Servlet> servlet) {
        this.servlet = servlet;
    }

    public void setMatching(MappingMatch matching) {
        this.matching = matching;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public UrlRequestHandler(ServletRegistrationDynamicImpl reg) {
        InstanceManager<? extends Servlet> s = reg.getServlet();
        ServletConfigImpl servletConfig = reg.getServletConfig();
        s = InstanceManager.postConstruct(s, servletConfig::initServlet);
        if (reg.getLoadOnStartup() < 0) {
            this.servlet = InstanceManager.lazy((v) -> this.servlet = v, s);
        } else {
            this.servlet = s;
        }
    }

    @Override
    public void doReq(RequestImpl request, ResponseImpl response) {
        try {
            if (matching == MappingMatch.EXTENSION) {
                if (request.getRequestURI().endsWith(pattern)) {
                    response.sendError(404);
                    return;
                }
            }
            servlet.get().service(request, response);
            response.doResponse();
        } catch (ServletException | IOException e) {
            response.setStatus(300);
        }
    }



}
