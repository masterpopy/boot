package personal.popy.localserver.io;

import java.nio.ByteBuffer;

public class InputBuffer {
    protected int mark;
    protected int offset;
    private byte[] bytes;

    final ByteBuffer buffer;//this buffer is always in read mode

    protected InputBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
        construct();
    }

    public byte get(int idx) {
        return bytes[offset + idx];
    }

    public byte[] array() {
        return bytes;
    }

    public int offset() {
        return offset;
    }

    public int limit() {
        return buffer.position();
    }

    public void mark(int mark) {
        this.mark = mark;
    }

    public int getMark() {
        return mark;
    }

    public void discard(int count) {
        offset += count;
    }

    public void compact() {
        System.arraycopy(bytes, offset, bytes, 0, remaining());
        mark -= offset;
        offset = 0;
        buffer.position(remaining());
    }

    public int remaining() {
        return buffer.position() - offset;
    }


    protected void construct() {
        bytes = buffer.array();
        offset = 0;
    }

    public void reset() {
        offset = 0;
    }

    public static InputBuffer heap(int count) {
        return new InputBuffer(ByteBuffer.allocate(count));
    }

    public static InputBuffer direct(int count) {
        return new DirectInputBuffer(ByteBuffer.allocateDirect(count));
    }
}
