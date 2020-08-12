package personal.popy.localserver.action;

import personal.popy.localserver.servlet.RequestImpl;
import personal.popy.localserver.servlet.ResponseImpl;

public interface RequestHandler {
    void doReq(RequestImpl request, ResponseImpl response);
}
