package personal.popy.localserver.servlet.registry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ServletConfigImpl implements ServletConfig {
    private String servletName;
    private ServletContext servletContext;
    private Map<String, String> initParameters = Collections.emptyMap();

    public void setInitParameter(String s, String s1) {
        if (initParameters.isEmpty()) {
            initParameters = new HashMap<>();
        }
        initParameters.put(s, s1);
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }


    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }


    public Map<String, String> getInitParameter() {
        return initParameters;
    }
}
