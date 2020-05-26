package personal.popy.localserver.servlet;

import personal.popy.localserver.data.FastList;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ServletContextImpl implements ServletContext, ServletConfig {
    private String contextPath = "";
    private Servlet servlet;
    private DynamicImpl dynamic = new DynamicImpl();
    private Hashtable<String,String> parameters = new Hashtable<>();
    private FastList<Object> attr = new FastList<>();

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public ServletContext getContext(String uriPath) {
        if (uriPath.startsWith(contextPath))
            return this;
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public String getMimeType(String file) {
        return "";
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return null;
    }

    @Override
    public URL getResource(String path) {
        return getClassLoader().getResource(path);
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        try {
            URL resource = getResource(path);
            if (resource == null) return null;
            return resource.openStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return null;
    }

    @Override
    public Servlet getServlet(String name) throws ServletException {
        return servlet;
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        return null;
    }

    @Override
    public Enumeration<String> getServletNames() {
        return new Enumeration<String>() {
            boolean h = true;

            @Override
            public boolean hasMoreElements() {
                return h;
            }

            @Override
            public String nextElement() {
                h = false;
                return contextPath;
            }
        };
    }

    @Override
    public void log(String msg) {

    }

    @Override
    public void log(Exception exception, String msg) {

    }

    @Override
    public void log(String message, Throwable throwable) {

    }

    @Override
    public String getRealPath(String path) {
        return contextPath;
    }

    @Override
    public String getServerInfo() {
        return null;
    }

    @Override
    public String getServletName() {
        return contextPath;
    }

    @Override
    public ServletContext getServletContext() {
        return this;
    }

    @Override
    public String getInitParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return parameters.keys();
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        parameters.put(name, value);
        return true;
    }

    @Override
    public Object getAttribute(String name) {
        return attr.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return attr.names();
    }

    @Override
    public void setAttribute(String name, Object object) {
        attr.addIfNotExits(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        attr.remove(name);
    }

    @Override
    public String getServletContextName() {
        return contextPath;
    }

    private class DynamicImpl implements ServletRegistration.Dynamic {

        @Override
        public void setLoadOnStartup(int i) {

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
            return new HashSet<>(Arrays.asList(strings));
        }

        @Override
        public Collection<String> getMappings() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRunAsRole() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getClassName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setInitParameter(String s, String s1) {
            return ServletContextImpl.this.setInitParameter(s, s1);
        }

        @Override
        public String getInitParameter(String s) {
            return ServletContextImpl.this.getInitParameter(s);
        }

        @Override
        public Set<String> setInitParameters(Map<String, String> map) {
            map.forEach(ServletContextImpl.this::setInitParameter);
            return map.keySet();
        }

        @Override
        public Map<String, String> getInitParameters() {
            return ServletContextImpl.this.parameters;
        }
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {

        return dynamic;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        this.servlet = servlet;
        try {
            servlet.init(this);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return dynamic;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return dynamic;
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;
    }

    @Override
    public void addListener(String className) {

    }

    @Override
    public <T extends EventListener> void addListener(T t) {

    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {

    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return ServletContext.class.getClassLoader();
    }

    @Override
    public void declareRoles(String... roleNames) {

    }

    @Override
    public String getVirtualServerName() {
        return null;
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {

    }

    @Override
    public String getRequestCharacterEncoding() {
        return null;
    }

    @Override
    public void setRequestCharacterEncoding(String encoding) {

    }

    @Override
    public String getResponseCharacterEncoding() {
        return null;
    }

    @Override
    public void setResponseCharacterEncoding(String encoding) {

    }
}
