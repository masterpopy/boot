package personal.popy.server.exception;

public class ServerException extends RuntimeException {
    public static boolean DEBUG;

    @Override
    public synchronized Throwable fillInStackTrace() {
        if (DEBUG) {
            return super.fillInStackTrace();
        }
        return this;
    }

    public ServerException(String msg) {
        super(msg);
    }
}
