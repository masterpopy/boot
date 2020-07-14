package personal.popy.localserver.io;

public class AsyncInputController {
    private final AsyncSource source;

    private AsyncListener listener;

    public AsyncInputController(AsyncSource source) {
        this.source = source;
    }

    public void setListener(AsyncListener listener) {
        this.listener = listener;
    }

    public void read() {
        source.read(listener);
    }


}
