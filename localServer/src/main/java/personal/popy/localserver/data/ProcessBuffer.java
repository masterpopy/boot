package personal.popy.localserver.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public final class ProcessBuffer {
    private final ByteBuffer streamBuf;//4kb
    private final ByteBuffer commonBuffer;//8kb,这个东东可以用来POST请求的读，也可以用于写。记得及时清理。

    private final CharBuffer charBuf;

    public ProcessBuffer() {
        streamBuf = ByteBuffer.allocate(1024 * 4);
        commonBuffer = ByteBuffer.allocateDirect(1024 * 8);
        charBuf = CharBuffer.allocate(1024);
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


    public void clear() {
        commonBuffer.clear();
        streamBuf.clear();
        charBuf.clear();
    }

}
