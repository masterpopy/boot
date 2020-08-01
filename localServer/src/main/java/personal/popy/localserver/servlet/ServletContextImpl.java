package personal.popy.localserver.servlet;

import personal.popy.localserver.data.FastList;
import personal.popy.localserver.servlet.base.AbsServletContextImpl;
import personal.popy.localserver.servlet.manage.InstanceFactory;
import personal.popy.localserver.servlet.registry.ServletRegistrationDynamicImpl;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * 这个context的生命周期仅存在于启动阶段
 * 嵌入式版的servletcontext并没有意义
 */
public class ServletContextImpl extends AbsServletContextImpl implements ServletContext {

    private Hashtable<String, String> parameters = new Hashtable<>();

    private FastList<Object> attributes = new FastList<>();

    private HashMap<String, ServletRegistrationDynamicImpl> servletRegistrations = new HashMap<>(4);

    private InstanceFactory instanceFactory = new InstanceFactory(Thread.currentThread().getContextClassLoader());

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
    public String getRealPath(String path) {
        return path;
    }

    @Override
    public String getServerInfo() {
        return "";
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
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return attributes.names();
    }

    @Override
    public void setAttribute(String name, Object object) {
        attributes.addIfNotExits(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }




    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        return new ServletRegistrationDynamicImpl(servletName, className, instanceFactory.newInstance(className));
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return new ServletRegistrationDynamicImpl(servletName, servlet.getClass().getName(), instanceFactory.newInstance(servlet));
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return new ServletRegistrationDynamicImpl(servletName, servletClass.getName(), instanceFactory.newInstance(servletClass));
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        return instanceFactory.newInstance(clazz).get();
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {

        return servletRegistrations.get(servletName);
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return servletRegistrations;
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
        return instanceFactory.getClassLoader();
    }

    @Override
    public void declareRoles(String... roleNames) {

    }

    @Override
    public String getVirtualServerName() {
        return "";
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

    public void clean() {
        servletRegistrations = null;
        instanceFactory = null;
        attributes = null;
        parameters = null;
    }
}
