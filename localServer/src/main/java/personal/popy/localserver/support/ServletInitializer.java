package personal.popy.localserver.support;

import personal.popy.localserver.lifecycle.Http11ConnHandler;
import personal.popy.localserver.lifecycle.ServerContext;
import personal.popy.localserver.lifecycle.ServerInitializer;

public class ServletInitializer implements ServerInitializer {

    private Http11ConnHandler handler = new Http11ConnHandler();
    @Override
    public void onInitialzing(ServerContext context) {
        context.getConnectionContext().setHandler(handler);
    }

    @Override
    public void onInitialized(ServerContext context) {
        handler.processServletContext();
        handler.setServer(context);
    }

    public Http11ConnHandler getHandler() {
        return handler;
    }
}
