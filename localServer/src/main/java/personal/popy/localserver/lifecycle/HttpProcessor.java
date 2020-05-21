package personal.popy.localserver.lifecycle;

import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.servlet.ServletContextImpl;
import personal.popy.localserver.source.Child;
import personal.popy.localserver.source.Single;

import javax.servlet.ServletContext;
import java.nio.channels.AsynchronousSocketChannel;

@Single
public class HttpProcessor extends EnvAwire implements Processor {

    private ServletContext servletContext = new ServletContextImpl();
    public HttpProcessor() {


    }

    @Override
    public void processNewConnection(AsynchronousSocketChannel result) {
        HttpExchanger exchanger = new HttpExchanger(this, result);
        server.getConnectionContext().executeWork(exchanger);
    }


    @Child
    public ServletContext getServletContext() {
        return servletContext;
    }



}
