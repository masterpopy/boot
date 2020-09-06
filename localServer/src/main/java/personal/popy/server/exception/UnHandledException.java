package personal.popy.server.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class UnHandledException extends NonException {

    public UnHandledException(Throwable e) {
        super(e);
    }

    @Override
    public void printStackTrace() {
        getCause().printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        getCause().printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        getCause().printStackTrace(s);
    }
}
