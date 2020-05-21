package personal.popy.localserver.connect.io;

import personal.popy.localserver.connect.buffer.ResponseWriter;
import personal.popy.localserver.data.StaticBuffer;
import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public class BlockRespWriter implements ResponseWriter {

    private ByteBuffer buffer;

    public BlockRespWriter() {

    }


    private void flush(HttpExchanger exchanger) {
        if (buffer.position() == 0) {
            return;
        }
        buffer.flip();
        try {
            exchanger.getChannel().write(buffer).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buffer.clear();
    }

    public void doWrite(HttpExchanger exchanger, ByteBuffer b) {
        if (buffer == null) {
            buffer = StaticBuffer.allocByteBuffer();
        }
        int remaining = buffer.remaining();
        if (remaining < b.remaining()) {
            if (buffer.capacity() == 1024) {
                ByteBuffer n = StaticBuffer.allocByteBuffer8();
                buffer.flip();
                n.put(buffer);
                StaticBuffer.saveByteBuffer(buffer);
                buffer = n;
            } else {
                int limit = b.limit();
                b.limit(remaining);
                buffer.put(b);
                b.limit(limit);
                flush(exchanger);
            }
        }
        buffer.put(b);
    }

    public void end(HttpExchanger exchanger, ByteBuffer b) {
        if (buffer != null) {
            flush(exchanger);
            StaticBuffer.saveByteBuffer(buffer);
            buffer = null;
        }
        exchanger.run();
    }
}
