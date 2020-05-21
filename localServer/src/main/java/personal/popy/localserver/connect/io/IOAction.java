package personal.popy.localserver.connect.io;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public interface IOAction {

    <A> void doFunc(AsynchronousSocketChannel channel,
                    ByteBuffer[] dsts,
                    int offset,
                    int length,
                    long timeout,
                    TimeUnit unit,
                    A attachment,
                    CompletionHandler<Long, ? super A> handler);

    IOAction READ = AsynchronousSocketChannel::read;
    IOAction WRITE = AsynchronousSocketChannel::write;
}
