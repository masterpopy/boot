package personal.popy.localserver.io.stream;

import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public class LengthReader implements RequestReader {


    private int remaining;

    private HttpExchanger exchanger;

    private ByteBuffer buffer;

    public LengthReader(int contentLength, HttpExchanger exchanger) {
        this.remaining = contentLength;
        this.exchanger = exchanger;
        this.buffer = exchanger.getBuf().borrowByteBuffer();
        buffer.limit(0);
    }

    @Override
    public int doRead(byte[] b, int off, int len) {
        if (remaining <= 0) {
            return -1;
        }
        int nRead;

        if (buffer.hasRemaining()) {
            nRead = buffer.remaining();
        } else {
            if (remaining < buffer.capacity()) {//保证最大读取数不会超过剩余字节数。保证安全读取。
                buffer.position(0).limit(remaining);
                buffer = buffer.slice();
            }
            nRead = exchanger.doRead(buffer);
        }

        int length = Math.min(nRead, remaining);
        length = Math.min(len, length);
        remaining -= length;
        buffer.get(b, off, length);
        return length;
    }

    @Override
    public int doRead() {
        if (remaining <= 0) {
            return -1;
        }
        if (!buffer.hasRemaining()) {
            if (remaining < buffer.capacity()) {//保证最大读取数不会超过剩余字节数。保证安全读取。
                buffer.position(0).limit(remaining);
                buffer = buffer.slice();
            }
            exchanger.doRead(buffer);
            buffer.flip();
        }
        remaining--;
        return buffer.get();
    }

    @Override
    public boolean hasRemaining() {
        return remaining > 0;
    }

    @Override
    public void close() {

    }
}
