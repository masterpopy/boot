package personal.popy.localserver.support;

import personal.popy.localserver.lifecycle.Http11ConnHandler;
import personal.popy.localserver.lifecycle.Lifecycle;
import personal.popy.localserver.lifecycle.WebServerApplication;

public class ServletInitializer implements Lifecycle {

    private Http11ConnHandler handler = new Http11ConnHandler();
    @Override
    public void init(WebServerApplication context) {
        context.getConnectionContext().setHandler(handler);
    }

    @Override
    public void start(WebServerApplication context) {
        handler.processServletContext();
        handler.setServer(context);
    }

    public Http11ConnHandler getHandler() {
        return handler;
    }
}
