package personal.popy.localserver.servlet;

import personal.popy.localserver.servlet.base.AbsServletContextImpl;
import personal.popy.localserver.servlet.registry.ServletRegistrationDynamicImpl;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
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

    private Hashtable<String, Object> attributes = new Hashtable<>();

    private HashMap<String, ServletRegistrationDynamicImpl> servletRegistrations = new HashMap<>(4);

    @Override
    public String getMimeType(String file) {
        return "";
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return Collections.emptySet();
    }

    @Override
    public URL getResource(String path) {
        return getClassLoader().getResource(path);
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        try {
            URL resource = getResource(path);
            if (resource == null) {
                return null;
            }
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
        if (name == null) return null;
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
        return attributes.keys();
    }

    @Override
    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }


    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        ServletRegistrationDynamicImpl reg = new ServletRegistrationDynamicImpl(servletName, servlet.getClass().getName(), servlet, this);
        servletRegistrations.put(servletName, reg);
        return reg;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return servletRegistrations.get(servletName);
    }

    @Override
    public Map<String, ServletRegistrationDynamicImpl> getServletRegistrations() {
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
        return Thread.currentThread().getContextClassLoader();
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
        parameters = null;
    }
}
