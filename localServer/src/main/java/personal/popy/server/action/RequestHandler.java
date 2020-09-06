package personal.popy.server.action;

import personal.popy.server.servlet.RequestImpl;
import personal.popy.server.servlet.ResponseImpl;

public interface RequestHandler {
    void doReq(RequestImpl request, ResponseImpl response);
}
