package personal.popy.localserver.action;

import personal.popy.localserver.servlet.RequestImpl;
import personal.popy.localserver.servlet.ResponseImpl;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.MappingMatch;
import java.io.IOException;

public class UrlRequestHandler implements RequestHandler {

    private Servlet servlet;
    private MappingMatch matching;
    private String pattern;

    UrlRequestHandler(Servlet servlet) {
        this.servlet = servlet;
    }

    void setMatching(MappingMatch matching) {
        this.matching = matching;
    }

    void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void doReq(RequestImpl request, ResponseImpl response) {
        try {
            if (matching == MappingMatch.EXTENSION) {
                if (request.getRequestURI().endsWith(pattern)) {
                    response.sendError(404);
                    return;
                }
            }
            servlet.service(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            response.setStatus(300);
        }
        response.doResponse();
    }



}
