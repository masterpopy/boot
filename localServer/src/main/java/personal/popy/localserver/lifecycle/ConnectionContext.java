package personal.popy.localserver.lifecycle;

import personal.popy.localserver.connect.aio.AioAcceptor;
import personal.popy.localserver.executor.ExecutorFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;


public class ConnectionContext extends EnvAwire implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private int port = 8080;

    private ExecutorService worker = ExecutorFactory.newInstance(20);

    private ExecutorService ioExecutor = ExecutorFactory.newIo();

    public void start() throws Exception {
        worker.execute(() -> {
        });
        ioExecutor.execute(() -> {
        });
        AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withThreadPool(ioExecutor);
        AsynchronousServerSocketChannel aio = AsynchronousServerSocketChannel.open(threadGroup);
        aio.bind(new InetSocketAddress(port), 100);
        AioAcceptor acceptor = new AioAcceptor(aio, this);
        acceptor.run();
    }

    @Override
    public void completed(AsynchronousSocketChannel result, Void attachment) {
        server.getProcessor().processNewConnection(result, this);
    }

    public ExecutorService getWorker() {
        return worker;
    }

    public void stop() {
        ioExecutor.shutdown();
        worker.shutdown();
    }

    public void executeWork(HttpWorker runnable) {
        worker.execute(runnable);
    }


    @Override
    public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace();
    }

    public int getPort() {
        return port;
    }
}
