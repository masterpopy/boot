package personal.popy.localserver.connect.io;

import personal.popy.localserver.connect.buffer.ResponseWriter;
import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.util.TimeMonitor;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class BlockRespWriter extends TimeMonitor implements ResponseWriter, CompletionHandler<Integer, HttpExchanger> {

    private ByteBuffer buffer;


    public BlockRespWriter() {

    }


    private void flush(HttpExchanger exchanger, boolean block) {
        if (buffer.position() == 0) {
            return;
        }
        buffer.flip();
        try {
            timeStart();

            if (block) {
                Future<Integer> write = exchanger.getChannel().write(buffer);
                write.get();
                buffer.clear();
            } else {
                exchanger.getChannel().write(buffer, exchanger, this);
            }
            timeEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doWrite(HttpExchanger exchanger, ByteBuffer b) {
        if (buffer == null) {
            buffer = exchanger.getBuf().getWriterBuf();
        }
        int remaining = buffer.remaining();
        if (remaining < b.remaining()) {
            int limit = b.limit();
            b.limit(remaining);
            buffer.put(b);
            b.limit(limit);
            flush(exchanger, true);
        }
        buffer.put(b);
    }

    public void end(HttpExchanger exchanger, ByteBuffer b) {
        if (buffer != null) {
//            AsynchronousSocketChannel channel = exchanger.getChannel();
            flush(exchanger, false);
            /*ServerContext server = exchanger.getServer();
            server.getProcessor().processNewConnection(channel, server.getConnectionContext());*/
        }
    }

    @Override
    public void completed(Integer result, HttpExchanger exchanger) {
        exchanger.end();
        buffer = null;
        exchanger.getServer().getConnectionContext().executeWork(exchanger);
    }

    @Override
    public void failed(Throwable exc, HttpExchanger attachment) {
        exc.printStackTrace();
    }
}
