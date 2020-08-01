package personal.popy.localserver.action;

import personal.popy.localserver.servlet.ServletContextImpl;
import personal.popy.localserver.servlet.util.RestPath;

import javax.servlet.ServletRegistration;
import java.util.Map;

public class HttpUrlWrapper {

    private RestPath<RequestHandler> wrapper;

    public void prepareProcessContext(ServletContextImpl servletContext) {
        //添加mapping
        Map<String, ? extends ServletRegistration> servletRegistrations = servletContext.getServletRegistrations();
        //初始化servlet

        servletContext.clean();
    }
}
