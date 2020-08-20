package personal.popy.localserver.lifecycle;

import personal.popy.localserver.action.HttpUrlWrapper;
import personal.popy.localserver.data.SynchronizedStack;
import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.servlet.RequestImpl;
import personal.popy.localserver.servlet.ResponseImpl;
import personal.popy.localserver.servlet.ServletContextImpl;

import javax.servlet.ServletContext;
import java.nio.channels.AsynchronousSocketChannel;

//这个类负责处理新进来的连接，并且管理http交换器HttpExchanger
public class Http11ConnHandler extends EnvAwire implements ConnHandler {

    private ServletContextImpl servletContext = new ServletContextImpl();

    private HttpUrlWrapper urlWrapper = new HttpUrlWrapper();

    public static final SynchronizedStack<HttpExchanger> stack = new SynchronizedStack<>(128,500);
    public Http11ConnHandler() {
    }

    @Override
    public void processNewConnection(AsynchronousSocketChannel result, ConnectionContext connectionContext) {
        HttpExchanger pop = stack.pop();
        if (pop == null) {
            pop = new HttpExchanger(this, result);
        } else {
            pop.setChannel(result);
        }
        pop.doParse();
    }


    public ServletContext getServletContext() {
        return servletContext;
    }

    public void endRequest(HttpExchanger exchanger) {
        stack.push(exchanger);
    }

    public void processServletContext(){
        urlWrapper.prepareProcessContext(servletContext);
    }

    public void processRequest(RequestImpl request, ResponseImpl response) {
        urlWrapper.processReq(request, response);
    }

}
