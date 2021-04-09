package personal.popy.server.io.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public abstract class PollerEvent {
    public int ops;

    public final SocketChannel ch;

    public Object obj;

    public long readTimeout;

    public long writeTimeout;

    public long lastRead;

    public long lastWrite;

    public PollerEvent(SocketChannel ch) {
        if (ch == null)
            throw new NullPointerException();
        this.ch = ch;
    }

    protected abstract void process(int readyOps);

    protected boolean processReadTimeout() {
        return false;
    }

    public boolean processWriteTimeout() {
        return false;
    }

    protected void cancelled() {
        try {
            ch.close();
        } catch (IOException e) {

        }
    }
}
