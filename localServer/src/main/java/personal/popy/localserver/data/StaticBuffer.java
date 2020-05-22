package personal.popy.localserver.data;

import java.nio.ByteBuffer;

public class StaticBuffer {
    public static final GlobalBuffered buffered = new GlobalBuffered();

    public static ByteBuffer allocByteBuffer() {
        return buffered.allocByteBuffer();
    }


    public static void saveByteBuffer(ByteBuffer buffer) {
        buffered.saveByteBuffer(buffer);
    }



}
