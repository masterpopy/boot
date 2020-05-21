package personal.popy.localserver.connect.io;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class ChannelWrapper {
    private AsynchronousSocketChannel channel;

    private ByteBuffer[] buf = new ByteBuffer[1];

    private int timeout;

    private class ChannelData implements Runnable {
        private ByteBuffer b;
        private CompletionHandler<Long, ByteBuffer> c;
        private IOAction action;

        @Override
        public void run() {
            buf[0] = b;
            action.doFunc(channel, buf, 0, 1, timeout, TimeUnit.MILLISECONDS, b, c);
        }
    }

    public void setChannel(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public void doFunc(ByteBuffer b, CompletionHandler<Integer, ByteBuffer> c, Executor e) {

    }

}
