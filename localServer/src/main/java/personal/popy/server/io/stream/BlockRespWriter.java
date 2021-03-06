package personal.popy.server.io.stream;

import personal.popy.server.servlet.HttpExchanger;
import personal.popy.server.util.TimeMonitor;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

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
            Future<Integer> write = exchanger.getChannel().write(buffer);
            write.get();
            timeEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buffer.clear();
        buffer = null;
    }

    public void doWrite(HttpExchanger exchanger, ByteBuffer b) {
        if (buffer == null) {
            buffer = exchanger.getBuf().borrowByteBuffer();
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

    public void doEnd(HttpExchanger exchanger, ByteBuffer b) {
        if (buffer != null) {
            flush(exchanger);
            buffer = null;
            exchanger.doParse();
        }
    }

    public void doEnd() {

    }

}
