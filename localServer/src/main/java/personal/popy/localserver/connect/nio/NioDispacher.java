package personal.popy.localserver.connect.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.concurrent.Executor;

public class NioDispacher implements NioHandler {
    private SocketChannel channel;
    private ByteBuffer buffer;
    private Executor executor;


    private CompletionHandler<Integer, ByteBuffer> handler;

    public NioDispacher(SocketChannel channel) {
        this.channel = channel;
    }

    public static void closeChannel(AbstractInterruptibleChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
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
            executor.execute(() -> {
                try {
                    int read = channel.read(buffer);
                    handler.completed(read, buffer.duplicate());
                } catch (Throwable t) {
                    key.cancel();
                    closeChannel(channel);
                }
            });
            channel.register(key.selector(), SelectionKey.OP_READ, this);
        } catch (IOException e) {
            executor.execute(() -> handler.failed(e, buffer));
            key.cancel();
        }
    }
}
