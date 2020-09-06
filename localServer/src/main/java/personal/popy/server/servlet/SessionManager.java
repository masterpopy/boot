package personal.popy.server.servlet;

import personal.popy.server.servlet.util.SecureRandomSessionIdGenerator;
import personal.popy.server.servlet.util.SessionIdGenerator;

import java.util.HashMap;

public class SessionManager {
    private static SessionManager instance = new SessionManager();
    private HashMap<String, SessionImpl> sessions = new HashMap<>();

    private SessionIdGenerator idGenerator = new SecureRandomSessionIdGenerator();

    public static SessionManager getInstance(){
        return instance;
    }


    public SessionImpl createSession() {
        String id;
        while (sessions.containsKey((id = idGenerator.createSessionId()))) {

        }
        return new SessionImpl(id);
    }

    public void invalidSession(SessionImpl session, String id) {
        sessions.remove(id);
    }
}
