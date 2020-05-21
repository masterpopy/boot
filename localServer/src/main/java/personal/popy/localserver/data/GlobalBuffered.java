package personal.popy.localserver.data;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class GlobalBuffered {
    private static final int limit = 1024;
    public final SynchronizedStack<ByteBuffer> bufferBytes = new SynchronizedStack<>(128, limit);
    public final SynchronizedStack<CharBuffer> bufferChars = new SynchronizedStack<>(128, limit);
    public final SynchronizedStack<ByteBuffer> bufferBytes8 = new SynchronizedStack<>(128, limit);


    public ByteBuffer allocByteBuffer() {
        ByteBuffer pop = bufferBytes.pop();
        if (pop == null) {
            return ByteBuffer.allocate(1024);
        }
        return pop;
    }

    public ByteBuffer allocByteBuffer8() {

        ByteBuffer pop = bufferBytes8.pop();
        if (pop == null) {
            return ByteBuffer.allocate(1024 * 8);
        }
        return pop;
    }


    public void saveByteBuffer(ByteBuffer buffer) {
        if (buffer == null) {
            return;
        }
        buffer.clear();
        if (buffer.capacity() == 1024) {
            bufferBytes.push(buffer);
        } else if (buffer.capacity() == 1024 * 8) {
            bufferBytes8.push(buffer);
        }
    }


    public void saveCharBuffer(CharBuffer buffer) {
        if (buffer == null) return;
        buffer.clear();
        bufferChars.push(buffer);
    }


    public CharBuffer allocCharBuffer() {
        CharBuffer pop = bufferChars.pop();
        if (pop == null) {
            return CharBuffer.allocate(1024);
        }
        return pop;
    }


    public CharBuffer allocCharBuffer8() {
        CharBuffer pop = bufferChars.pop();
        if (pop == null) {
            return CharBuffer.allocate(1024 * 8);
        }
        return pop;
    }
}
