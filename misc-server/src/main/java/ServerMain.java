import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    public static volatile boolean RUN = true;

    public static void main(String[] args) throws Exception {

        AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withThreadPool(new ThreadPoolExecutor(1, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>()));
        AsynchronousServerSocketChannel aio = AsynchronousServerSocketChannel.open(threadGroup);
        aio.bind(new InetSocketAddress(8888), 100);

        AcceptorHandler handler = new AcceptorHandler();
        while (RUN) {
            AsynchronousSocketChannel channel = aio.accept().get();
            handler.completed(channel, null);
        }

    }
}
