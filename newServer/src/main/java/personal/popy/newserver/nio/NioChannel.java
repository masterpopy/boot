package personal.popy.newserver.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioChannel {
    int ops;

    final SocketChannel ch;

    long readTimeout;

    long writeTimeout;

    long lastRead;

    long lastWrite;

    public NioChannel(SocketChannel ch) {
        if (ch == null)
            throw new NullPointerException();
        this.ch = ch;
    }

    protected void process(int readyOps) {

    }

    protected boolean processReadTimeout() {
        return false;
    }

    public boolean processWriteTimeout() {
        return false;
    }

    protected void cancelled() {
        try {
            ch.close();
        } catch (IOException ignored) {

        }
    }

    public void updateLastRead() {
        lastRead = System.currentTimeMillis();
    }

    public void updateLastWrite() {
        lastWrite = System.currentTimeMillis();
    }

    public int ops() {
        return ops;
    }

    public void ops(int ops) {
        this.ops = ops;
    }

    public int read(ByteBuffer b) throws IOException {
        updateLastRead();
        return ch.read(b);
    }

    public long read(ByteBuffer[] b) throws IOException {
        updateLastRead();
        return ch.read(b);
    }

    public int write(ByteBuffer b) throws IOException {
        updateLastWrite();
        return ch.write(b);
    }

    public long write(ByteBuffer[] b) throws IOException {
        updateLastWrite();
        return ch.write(b);
    }
}
