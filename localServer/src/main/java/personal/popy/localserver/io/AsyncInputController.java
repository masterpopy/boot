package personal.popy.localserver.io;

import java.nio.ByteBuffer;

public class AsyncInputController {
    private final AsyncSource source;

    private AsyncListener listener;

    private ByteBuffer buffer;

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
