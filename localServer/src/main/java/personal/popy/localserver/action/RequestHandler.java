package personal.popy.localserver.action;

import personal.popy.localserver.servlet.HttpExchanger;

public interface RequestHandler {
    void doReq(HttpExchanger exchanger);
}
