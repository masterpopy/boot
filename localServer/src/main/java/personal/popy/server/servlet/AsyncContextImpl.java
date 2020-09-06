package personal.popy.server.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AsyncContextImpl implements AsyncContext {
    private RequestImpl request;

    private RequestImpl servletRequest;
    private ResponseImpl servletResponse;


    @Override
    public ServletRequest getRequest() {
        return servletRequest;
    }

    @Override
    public ServletResponse getResponse() {
        return servletResponse;
    }

    @Override
    public boolean hasOriginalRequestAndResponse() {
        return request == servletRequest && request.getExchanger().createResponse() == servletResponse;
    }

    @Override
    public void dispatch() {

    }

    @Override
    public void dispatch(String s) {

    }

    @Override
    public void dispatch(ServletContext servletContext, String s) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void start(Runnable runnable) {

    }

    @Override
    public void addListener(AsyncListener asyncListener) {

    }

    @Override
    public void addListener(AsyncListener asyncListener, ServletRequest servletRequest, ServletResponse servletResponse) {

    }

    @Override
    public <T extends AsyncListener> T createListener(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public void setTimeout(long l) {

    }

    @Override
    public long getTimeout() {
        return 0;
    }
}
