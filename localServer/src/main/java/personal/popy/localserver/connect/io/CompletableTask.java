package personal.popy.localserver.connect.io;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

public class CompletableTask extends CompletableFuture<ByteBuffer> implements CompletionHandler<Long, ByteBuffer>, Runnable {
    @Override
    public void run() {

    }

    @Override
    public void completed(Long result, ByteBuffer attachment) {

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
