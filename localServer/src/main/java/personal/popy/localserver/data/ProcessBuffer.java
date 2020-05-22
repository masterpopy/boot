package personal.popy.localserver.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public final class ProcessBuffer {
    private final ByteBuffer streamBuf;//4kb
    private final ByteBuffer writerBuf;//8kb

    public static final ThreadLocal<CharBuffer> charBuf = ThreadLocal.withInitial(() -> CharBuffer.allocate(1024));


    public ProcessBuffer() {
        streamBuf = ByteBuffer.allocate(1024 * 4);
        writerBuf = ByteBuffer.allocate(1024 * 8);
    }


    public ByteBuffer getStreamBuf() {
        return streamBuf;
    }

    public ByteBuffer getWriterBuf() {
        return writerBuf;
    }

    public ThreadLocal<CharBuffer> getCharBuf() {
        return charBuf;
    }

    private static final SynchronizedStack<ProcessBuffer> stack = new SynchronizedStack<>();


    public static ProcessBuffer alloc() {
        ProcessBuffer pop = stack.pop();
        if (pop == null) {
            return new ProcessBuffer();
        }
        return pop;
    }

    public void save() {
        clear();
        stack.push(this);
    }

    public void clear() {
        writerBuf.clear();
        streamBuf.clear();
    }
}
