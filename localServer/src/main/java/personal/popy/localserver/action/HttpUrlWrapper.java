package personal.popy.localserver.action;

import personal.popy.localserver.servlet.ServletContextImpl;
import personal.popy.localserver.servlet.registry.ServletRegistrationDynamicImpl;
import personal.popy.localserver.servlet.util.RestPath;

import java.util.Map;

public class HttpUrlWrapper {

    private RestPath<RequestHandler> wrapper = new RestPath<>();

    public void prepareProcessContext(ServletContextImpl servletContext) {

        //初始化servlet
        processUrlPattern(servletContext.getServletRegistrations());
        servletContext.clean();
    }

    private void processUrlPattern(Map<String, ServletRegistrationDynamicImpl> servletRegistrations) {
        for (ServletRegistrationDynamicImpl value : servletRegistrations.values()) {
            String mapping = value.getMapping();
            int pos = mapping.indexOf('*');
            if (pos >= 0) {
                int length = mapping.length();
                //重复的直接覆盖。
                if (pos == length - 1) {//prefix mapping
                    wrapper.addPrefixPath(mapping.substring(0, pos - 1), new UrlRequestHandler());
                } else if (pos < length - 1 && mapping.charAt(pos + 1) == '.') { //extension mapping
                    String url = mapping.substring(0, pos - 1);
                    String extension = mapping.substring(pos + 1);
                    wrapper.addPrefixPath(url, new UrlRequestHandler());
                }
            } else {
                wrapper.addExactPath(mapping, new UrlRequestHandler());
            }
        }
    }
}
