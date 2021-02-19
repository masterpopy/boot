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
    public void read(ByteBuffer b, Consumer<Integer> ha) {
        if (ha == null) {
            try {
                do {
                    channel.read(b).get();
                } while (b.hasRemaining());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                close();
                handle.accept(e);
                throw new RuntimeException();
            }
        } else {
            readHandle = ha;
            channel.read(b, null, this);
        }

    }

    @Override
    public void write(ByteBuffer b) {
        try {
            do {
                channel.write(b).get();
            } while (b.hasRemaining());
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
