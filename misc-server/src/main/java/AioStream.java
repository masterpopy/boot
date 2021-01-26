import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

public class AioStream implements IoStream {

    private final AsynchronousSocketChannel channel;

    public AioStream(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }


    @Override
    public int read(ByteBuffer b) {
        try {
            return channel.read(b).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void write(ByteBuffer b) {
        try {
            channel.write(b).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void read(ByteBuffer b, Master master, CompletionHandler<Integer, Master> handler) {
        channel.read(b, master, handler);
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
