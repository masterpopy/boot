package personal.popy.localserver.lifecycle;

import personal.popy.localserver.data.SynchronizedStack;
import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.servlet.ServletContextImpl;
import personal.popy.localserver.source.Child;
import personal.popy.localserver.source.Single;

import javax.servlet.ServletContext;
import java.nio.channels.AsynchronousSocketChannel;

@Single
public class HttpProcessor extends EnvAwire implements Processor {

    private ServletContext servletContext = new ServletContextImpl();

    private static final SynchronizedStack<HttpExchanger> stack = new SynchronizedStack<>(128,500);
    public HttpProcessor() {
    }

    @Override
    public void processNewConnection(AsynchronousSocketChannel result) {
        HttpExchanger pop = stack.pop();
        if (pop == null) {
            pop = new HttpExchanger(this, result);
        } else {
            pop.setChannel(result);
        }

        pop.run();
    }


    @Child
    public ServletContext getServletContext() {
        return servletContext;
    }

    public void endRequest(HttpExchanger exchanger) {
        exchanger.end();
        stack.push(exchanger);
    }

}
