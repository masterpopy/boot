import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class AioStream implements IoStream, CompletionHandler<Integer, Void> {

    private final AsynchronousSocketChannel channel;
    private Consumer<Throwable> handle;
    private Consumer<Integer> readHandle;

    public AioStream(AsynchronousSocketChannel channel, Consumer<Throwable> t) {
        this.channel = channel;
        handle = t;
    }


    @Override
    public int read(ByteBuffer b, Consumer<Integer> ha) {
        if (ha == null) {
            try {
                return channel.read(b).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                close();
                handle.accept(e);
                throw new RuntimeException();
            }
        } else {
            readHandle = ha;
            channel.read(b, null, this);
            return 0;
        }

    }

    @Override
    public void write(ByteBuffer b) {
        try {
            channel.write(b).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            close();
            handle.accept(e);
            throw new RuntimeException();
        }
    }


    @Override
    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Integer result, Void attachment) {
        readHandle.accept(result);
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        handle.accept(exc);
    }
}
