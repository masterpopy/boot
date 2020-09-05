package personal.popy.localserver.lifecycle;

import personal.popy.localserver.exception.UnHandledException;
import personal.popy.localserver.executor.ExecutorFactory;
import personal.popy.localserver.io.aio.AioAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;


public class ConnectionContext extends EnvAwire implements CompletionHandler<AsynchronousSocketChannel, Void>, ServerWorker {
    private int port = 8080;

    private ExecutorService worker = ExecutorFactory.newInstance(20);

    private ExecutorService ioExecutor = ExecutorFactory.newIo();

    private ConnHandler handler;

    private AioAcceptor acceptor;

    public void init() {
        worker.execute(() -> {
        });
        ioExecutor.execute(() -> {
        });
        try {
            AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withThreadPool(ioExecutor);
            AsynchronousServerSocketChannel aio = AsynchronousServerSocketChannel.open(threadGroup);
            aio.bind(new InetSocketAddress(port), 100);
            acceptor = new AioAcceptor(aio, this);
        } catch (IOException e){
            //这个错误大概率不会发生
            throw new UnHandledException(e);
        }

    }

    @Override
    public void run() {
        acceptor.run();
    }

    @Override
    public void completed(AsynchronousSocketChannel result, Void attachment) {
        handler.processNewConnection(result);
    }

    public ExecutorService getWorker() {
        return worker;
    }

    public void destroy() {
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

    public void setHandler(ConnHandler handler) {
        this.handler = handler;
    }

}
