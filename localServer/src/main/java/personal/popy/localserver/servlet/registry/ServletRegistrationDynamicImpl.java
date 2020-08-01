package personal.popy.localserver.servlet.registry;

import org.graalvm.compiler.nodes.calc.CompareNode;
import personal.popy.localserver.servlet.manage.InstanceHandler;

import javax.servlet.*;
import java.util.*;

public class ServletRegistrationDynamicImpl implements ServletRegistration.Dynamic {

    private final String className;
    private int loadOnStartup;
    private String mapping;


    private InstanceHandler<? extends Servlet> instance;


    private ServletConfigImpl servletConfig = new ServletConfigImpl();

    public ServletRegistrationDynamicImpl(String name, String className, InstanceHandler<? extends Servlet> instance) {
        servletConfig.setServletName(name);
        this.instance = instance;
        this.className = className;
    }

    @Override
    public void setLoadOnStartup(int i) {
        this.loadOnStartup = i;
    }

    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    @Override
    public Set<String> setServletSecurity(ServletSecurityElement servletSecurityElement) {
        return null;
    }

    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfigElement) {

    }

    @Override
    public void setRunAsRole(String s) {

    }

    @Override
    public void setAsyncSupported(boolean b) {

    }

    @Override
    public Set<String> addMapping(String... strings) {
        if (strings.length != 1 || mapping != null) {
            throw new IllegalArgumentException("每个servlet只支持一个url pattern");
        }
        mapping = strings[0];
        return getMappings();
    }

    @Override
    public Set<String> getMappings() {
        return Collections.singleton(mapping);
    }

    @Override
    public String getRunAsRole() {
        return null;
    }

    @Override
    public String getName() {
        return servletConfig.getServletName();
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public boolean setInitParameter(String s, String s1) {
        servletConfig.setInitParameter(s, s1);
        return true;
    }

    @Override
    public String getInitParameter(String s) {
        return servletConfig.getInitParameter(s);
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> map) {
        servletConfig.setInitParameters(map);
        return Collections.emptySet();
    }

    @Override
    public Map<String, String> getInitParameters() {
        return servletConfig.getInitParameter();
    }

    public InstanceHandler<? extends Servlet> getServlet() {
        return instance;
    }

    public String getMapping() {
        return mapping;
    }

    public ServletConfigImpl getServletConfig() {
        return servletConfig;
    }
}
