package personal.popy.localserver.io;

import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AioSource implements AsyncSource, CompletionHandler<Integer, ByteBuffer> {
    private HttpExchanger exchanger;
    private AsyncListener asyncListener;

    public AioSource(HttpExchanger exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void read(AsyncListener buffer) {
        this.asyncListener = buffer;
    }

    private void doRead() {
        exchanger.require(asyncListener.getBuffer(), this);
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (asyncListener.signal()) {
            doRead();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
