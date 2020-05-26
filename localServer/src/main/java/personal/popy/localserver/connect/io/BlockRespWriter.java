package personal.popy.localserver.connect.io;

import personal.popy.localserver.connect.buffer.ResponseWriter;
import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockRespWriter implements ResponseWriter {

    private ByteBuffer buffer;

    private static final List<AtomicInteger> ints = new ArrayList<>(40);

    private static ThreadLocal<AtomicInteger> s = ThreadLocal.withInitial(() -> {
        AtomicInteger i = new AtomicInteger();
        BlockRespWriter.ints.add(i);
        return i;
    });

    public static void cal() {
        int i = 0;
        for (AtomicInteger anInt : ints) {
            i += anInt.get();
        }
        System.out.println(i);
    }

    public BlockRespWriter() {

    }


    private void flush(HttpExchanger exchanger) {
        if (buffer.position() == 0) {
            return;
        }
        buffer.flip();
        try {
            long time = System.currentTimeMillis();
            exchanger.getChannel().write(buffer).get();
            AtomicInteger i = s.get();
            int l = (int) (System.currentTimeMillis() - time);
            i.set(l + i.get());
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
