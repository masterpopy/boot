package personal.popy.localserver.servlet;

import personal.popy.localserver.servlet.data.HeaderList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

public class SessionImpl implements HttpSession {

    private final String id;
    private long createTime = System.currentTimeMillis();
    private long lastAccessedTime = createTime;
    private ServletContext servletContext;

    private int maxInterval;

    private HeaderList<Object> attributes;


    public SessionImpl(String id) {
        this.id = id;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public long getCreationTime() {
        return createTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        maxInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        if (attributes == null)
            return null;
        return attributes.get(name);
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        if (attributes == null)
            return null;
        return attributes.names();
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (attributes == null) {
            attributes = new HeaderList<>(4);
        }
        attributes.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {

    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void removeValue(String name) {

    }

    @Override
    public void invalidate() {
        SessionManager.getInstance().invalidSession(this, id);
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
