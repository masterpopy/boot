package personal.popy.localserver.connect.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NioReader implements NioHandler{
    private SocketChannel channel;
    private ByteBuffer buffer;

    private CompletionHandler<Integer, ByteBuffer> handler;

    public NioReader(SocketChannel channel) {
        this.channel = channel;
    }

    public void setHandler(CompletionHandler<Integer, ByteBuffer> handler) {
        this.handler = handler;
    }

    @Override
    public void handle(SelectionKey key) {
        if (buffer == null) {
            //new request
            buffer = ByteBuffer.allocate(1024);
        }
        try {
            int read = channel.read(buffer);
            handler.completed(read, buffer);
            channel.register(key.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            handler.failed(e, buffer);
            key.cancel();
        }
    }
}
