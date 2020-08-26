package personal.popy.localserver.servlet;

import personal.popy.localserver.lifecycle.ConnectionContext;
import personal.popy.localserver.lifecycle.HttpWorker;
import personal.popy.localserver.servlet.data.CookieParser;
import personal.popy.localserver.util.TimeMonitor;
import personal.popy.localserver.wrapper.HttpReqEntity;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class RequestImpl extends TimeMonitor implements HttpServletRequest, HttpWorker {


    private final HttpExchanger exchanger;
    private HttpReqEntity entity;


    private SessionImpl session;

    private ServletInputStream inputStream;

    private Hashtable<String, Object> attributes = new Hashtable<>();

    private String bodyEncoding = "UTF-8";

    public RequestImpl(HttpExchanger exchanger) {
        this.exchanger = exchanger;
    }

    public HttpExchanger getExchanger() {
        return exchanger;
    }

    public void recycle() {
        entity.recycle();
        inputStream = null;
    }


    private ConnectionContext getConnectionContext() {
        return exchanger.getServer().getConnectionContext();
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return CookieParser.parse(getHeader("cookie"));
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return entity.headers.get(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return entity.headers.getHeaders(s);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return entity.headers.names();
    }

    @Override
    public int getIntHeader(String s) {
        //todo
        return Integer.parseInt(entity.headers.get(s));
    }

    @Override
    public String getMethod() {
        return entity.getMethod();
    }

    @Override
    public String getPathInfo() {
        return getContextPath();
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return getServletContext().getContextPath();
    }

    @Override
    public String getQueryString() {
        return entity.query;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return entity.uri;
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        String scheme = getScheme();
        int port = getServerPort();
        if (port < 0) {
            // Work around java.net.URL bug
            port = 80;
        }

        url.append(scheme);
        url.append("://");
        url.append(getServerName());
        if ((scheme.equals("http") && (port != 80))
                || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(getRequestURI());

        return url;
    }

    @Override
    public String getServletPath() {
        return ""; //not support
    }

    @Override
    public HttpSession getSession(boolean b) {
        if (session != null) {
            return session;
        }
        //find Session
        return session = SessionManager.getInstance().createSession();
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return attributes.keys();
    }

    @Override
    public String getCharacterEncoding() {
        return bodyEncoding;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        this.bodyEncoding = s;
    }

    @Override
    public int getContentLength() {
        if (entity.getContentLength() < Integer.MAX_VALUE)
            return (int) entity.getContentLength();
        return -1;
    }

    @Override
    public long getContentLengthLong() {
        return entity.getContentLength();
    }

    @Override
    public String getContentType() {
        return entity.getContentType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null) {
            return inputStream;
        }
        return inputStream = new ServletInputStreamImpl(this);
    }

    @Override
    public String getParameter(String s) {
        //todo parse body
        return entity.parameters.get(s);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return entity.parameters.names();
    }

    @Override
    public String[] getParameterValues(String s) {
        return entity.parameters.getParameterValues(s);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return entity.parameters.parameters();
    }

    @Override
    public String getProtocol() {
        return entity.protocol;
    }

    @Override
    public String getScheme() {
        return "http";
    }

    @Override
    public String getServerName() {
        //get connector proxyName
        return exchanger.getServer().getProxyName();
    }

    @Override
    public int getServerPort() {
        //get connector port
        return getConnectionContext().getPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), bodyEncoding));
    }

    @Override
    public String getRemoteAddr() {
        return "localhost";
    }

    @Override
    public String getRemoteHost() {
        return "localhost";
    }

    @Override
    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }

    @Override
    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        throw new RuntimeException();
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return exchanger.getProcessor().getServletContext();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return startAsync(this, exchanger.createResponse());
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return DispatcherType.REQUEST;
    }

    public void doServlet(HttpReqEntity entity) {
        this.entity = entity;
        exchanger.exe(this);
    }

    @Override
    public void run() {
        try {
            timeStart();
            exchanger.refreshBuf();
            ResponseImpl response = exchanger.createResponse();
            exchanger.getProcessor().processRequest(this, response);
            timeEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
