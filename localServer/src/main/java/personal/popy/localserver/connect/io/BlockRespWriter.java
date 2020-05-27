package personal.popy.localserver.connect.io;

import personal.popy.localserver.connect.buffer.ResponseWriter;
import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.util.TimeMonitor;

import java.nio.ByteBuffer;

public class BlockRespWriter extends TimeMonitor implements ResponseWriter {

    private ByteBuffer buffer;


    public BlockRespWriter() {

    }


    private void flush(HttpExchanger exchanger) {
        if (buffer.position() == 0) {
            return;
        }
        buffer.flip();
        try {
            timeStart();
            exchanger.getChannel().write(buffer).get();
            timeEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buffer.clear();
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
            flush(exchanger);
        }
        buffer.put(b);
    }

    public void end(HttpExchanger exchanger, ByteBuffer b) {
        if (buffer != null) {
            flush(exchanger);
            buffer = null;
        }
    }
}
