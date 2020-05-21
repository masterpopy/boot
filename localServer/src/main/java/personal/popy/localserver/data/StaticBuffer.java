package personal.popy.localserver.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class StaticBuffer {
    public static final GlobalBuffered buffered = new GlobalBuffered();

    public static ByteBuffer allocByteBuffer() {
        return buffered.allocByteBuffer();
    }

    public static ByteBuffer allocByteBuffer8() {
        return buffered.allocByteBuffer8();
    }

    public static void saveByteBuffer(ByteBuffer buffer) {
        buffered.saveByteBuffer(buffer);
    }

    public static void saveCharBuffer(CharBuffer buffer) {
        buffered.saveCharBuffer(buffer);
    }

    public static CharBuffer allocCharBuffer() {
        return buffered.allocCharBuffer();
    }

}
