package personal.popy.localserver.action;

import personal.popy.localserver.servlet.ServletContextImpl;
import personal.popy.localserver.servlet.registry.ServletRegistrationDynamicImpl;
import personal.popy.localserver.servlet.util.RestPath;

import javax.servlet.http.MappingMatch;
import java.util.Map;
import java.util.Set;

public class HttpUrlWrapper {

    private RestPath<RequestHandler> wrapper = new RestPath<>();

    public void prepareProcessContext(ServletContextImpl servletContext) {

        //初始化servlet
        processUrlPattern(servletContext.getServletRegistrations());
        servletContext.clean();
    }

    private void processUrlPattern(Map<String, ServletRegistrationDynamicImpl> servletRegistrations) {
        for (ServletRegistrationDynamicImpl value : servletRegistrations.values()) {
            Set<String> mappings = value.getMappings();
            for (String mapping : mappings) {
                UrlRequestHandler handler = new UrlRequestHandler(value.getServlet());
                if ("/".equals(mapping)) {
                    handler.setMatching(MappingMatch.DEFAULT);
                    handler.setPattern("/");
                    wrapper.addPrefixPath("/", handler);
                }
                int pos = mapping.indexOf('*');
                if (pos >= 0) {
                    int length = mapping.length();
                    //重复的直接覆盖。
                    if (pos == length - 1) {//prefix mapping
                        handler.setMatching(MappingMatch.PATH);
                        handler.setPattern(mapping);
                        wrapper.addPrefixPath(mapping.substring(0, pos - 1), handler);
                    } else if (pos < length - 1 && mapping.charAt(pos + 1) == '.') { //extension mapping
                        String url = mapping.substring(0, pos - 1);
                        handler.setMatching(MappingMatch.EXTENSION);
                        handler.setPattern(mapping.substring(pos + 1));
                        wrapper.addPrefixPath(url, handler);
                    }
                } else {
                    handler.setMatching(MappingMatch.EXACT);
                    handler.setPattern(mapping);
                    wrapper.addExactPath(mapping, handler);
                }
            }

        }
    }
}
