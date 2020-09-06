package personal.popy.server.lifecycle;

import personal.popy.server.action.HttpUrlWrapper;
import personal.popy.server.data.SynchronizedStack;
import personal.popy.server.servlet.HttpExchanger;
import personal.popy.server.servlet.RequestImpl;
import personal.popy.server.servlet.ResponseImpl;
import personal.popy.server.servlet.ServletContextImpl;

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
    public void processNewConnection(AsynchronousSocketChannel result) {
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
