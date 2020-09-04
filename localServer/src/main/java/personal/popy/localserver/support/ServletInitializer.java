package personal.popy.localserver.support;

import personal.popy.localserver.lifecycle.Http11ConnHandler;
import personal.popy.localserver.lifecycle.Lifecycle;
import personal.popy.localserver.lifecycle.ServerContext;

public class ServletInitializer implements Lifecycle {

    private Http11ConnHandler handler = new Http11ConnHandler();
    @Override
    public void init(ServerContext context) {
        context.getConnectionContext().setHandler(handler);
    }

    @Override
    public void start(ServerContext context) {
        handler.processServletContext();
        handler.setServer(context);
    }

    public Http11ConnHandler getHandler() {
        return handler;
    }
}
