package personal.popy.server.io.aio;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioAcceptor implements CompletionHandler <AsynchronousSocketChannel, Void>{
    private AsynchronousServerSocketChannel serverSock;
    private CompletionHandler<AsynchronousSocketChannel, Void> handler;
    private boolean isRunning;

    public AioAcceptor(AsynchronousServerSocketChannel serverSock, CompletionHandler<AsynchronousSocketChannel, Void> handler) {
        this.serverSock = serverSock;
        this.handler = handler;
    }

    public void run() {
        if  (isRunning) {
            throw new IllegalStateException();
        }
        start();
        isRunning = true;
    }


    private void start() {
        serverSock.accept(null, this);
    }


    @Override
    public void completed(AsynchronousSocketChannel result, Void attachment) {
        start();
        handler.completed(result, attachment);
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        handler.failed(exc, attachment);
    }
}
