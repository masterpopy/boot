package personal.popy.localserver.data;

import personal.popy.localserver.util.UnSafeStrBuf;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public final class ThreadLocalBuffer {
    public static final ThreadLocal<ThreadLocalBuffer> buffers = ThreadLocal.withInitial(ThreadLocalBuffer::new);

    private final ByteBuffer streamBuf;//4kb
    private final ByteBuffer commonBuffer;//8kb,这个东东可以用来POST请求的读，也可以用于写。记得及时清理。

    private final CharBuffer charBuf;
    private final UnSafeStrBuf buf;

    public ThreadLocalBuffer() {
        streamBuf = ByteBuffer.allocate(1024 * 4);
        commonBuffer = ByteBuffer.allocateDirect(1024 * 8);
        charBuf = CharBuffer.allocate(1024);
        buf = new UnSafeStrBuf(charBuf.array());
    }


    public ByteBuffer getStreamBuf() {
        return streamBuf;
    }

    public ByteBuffer borrowByteBuffer() {
        return commonBuffer;
    }

    public CharBuffer getCharBuf() {
        return charBuf;
    }

    public UnSafeStrBuf getBuf() {
        return buf;
    }

    public void clear() {
        commonBuffer.clear();
        streamBuf.clear();
        charBuf.clear();
    }

}
