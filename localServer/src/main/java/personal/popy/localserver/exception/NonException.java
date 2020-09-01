package personal.popy.localserver.exception;

public class NonException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public NonException(Throwable e){
        super(e);
    }

}
