package personal.popy.localserver.servlet.registry;

import personal.popy.localserver.servlet.manage.InstanceHandler;

import javax.servlet.*;
import java.util.*;

public class ServletRegistrationDynamicImpl implements ServletRegistration.Dynamic {

    private final String className;
    private int loadOnStartup;
    private List<String> mapping = new ArrayList<>(1);


    private InstanceHandler<? extends Servlet> instance;
    private Servlet servlet;

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
        HashSet<String> c = new HashSet<>(Arrays.asList(strings));
        mapping.addAll(c);
        return c;
    }

    @Override
    public Collection<String> getMappings() {
        return mapping;
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

    public Servlet getServlet() {
        return servlet == null ? servlet = instance.get() : servlet;
    }
}
