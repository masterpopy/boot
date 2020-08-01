package personal.popy.localserver.servlet.base;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Enumeration;

public abstract class AbsServletContextImpl implements ServletContext {
    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public ServletContext getContext(String uriPath) {
        return this;
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
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return null;
    }

    @Override
    public Servlet getServlet(String name) throws ServletException {
        return null;
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        return null;
    }

    @Override
    public Enumeration<String> getServletNames() {
        return null;
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
    public String getServletContextName() {
        return "";
    }
}
