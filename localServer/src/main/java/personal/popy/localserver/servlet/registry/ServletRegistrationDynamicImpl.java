package personal.popy.localserver.servlet.registry;

import personal.popy.localserver.servlet.manage.InstanceHandler;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;
import java.util.*;

public class ServletRegistrationDynamicImpl implements ServletRegistration.Dynamic {

    private final String className;
    private int loadOnStartup;
    private List<String> mapping = new ArrayList<>(1);
    private Map<String, String> initParams = Collections.emptyMap();
    private String name;
    private InstanceHandler<? extends Servlet> instance;

    private Servlet servlet;

    public ServletRegistrationDynamicImpl(String name, String className, InstanceHandler<? extends Servlet> instance) {
        this.name = name;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public boolean setInitParameter(String s, String s1) {
        if (initParams.isEmpty()) initParams = new HashMap<>();
        initParams.put(s, s1);
        return true;
    }

    @Override
    public String getInitParameter(String s) {
        return initParams.get(s);
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> map) {
        this.initParams = map;
        return map.keySet();
    }

    @Override
    public Map<String, String> getInitParameters() {
        return initParams;
    }

    public Servlet getServlet() {
        return servlet == null ? servlet = instance.get() : servlet;
    }
}
