package personal.popy.localserver.servlet.registry;

import personal.popy.localserver.servlet.manage.InstanceManager;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServletRegistrationDynamicImpl implements ServletRegistration.Dynamic {

    private final String className;
    private int loadOnStartup;
    private HashSet<String> mapping = new HashSet<>();


    private InstanceManager<? extends Servlet> instance;


    private ServletConfigImpl servletConfig = new ServletConfigImpl();

    public ServletRegistrationDynamicImpl(String name, String className, InstanceManager<? extends Servlet> instance) {
        servletConfig.setServletName(name);
        this.instance = instance;
        this.className = className;
    }

    @Override
    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
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
    public void setRunAsRole(String runAsRole) {

    }

    @Override
    public void setAsyncSupported(boolean async) {

    }

    @Override
    public Set<String> addMapping(String... strings) {
        mapping.addAll(Arrays.asList(strings));
        return getMappings();
    }

    @Override
    public Set<String> getMappings() {
        return Collections.unmodifiableSet(mapping);
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
    public boolean setInitParameter(String key, String value) {
        servletConfig.setInitParameter(key, value);
        return true;
    }

    @Override
    public String getInitParameter(String key) {
        return servletConfig.getInitParameter(key);
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

    public InstanceManager<? extends Servlet> getServlet() {
        return instance;
    }

    public ServletConfigImpl getServletConfig() {
        return servletConfig;
    }
}
