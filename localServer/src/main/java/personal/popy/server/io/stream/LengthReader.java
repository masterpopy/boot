package personal.popy.server.io.stream;

import personal.popy.server.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public class LengthReader implements RequestReader {


    private int remaining;

    private final HttpExchanger exchanger;

    private ByteBuffer buffer;

    public LengthReader(int contentLength, HttpExchanger exchanger) {
        this.remaining = contentLength;
        this.exchanger = exchanger;
        this.buffer = exchanger.getBuf().borrowByteBuffer();
        buffer.limit(0);
    }

    private int doRead0() {
        buffer.position(0);
        if (remaining < buffer.capacity()) {//保证最大读取数不会超过剩余字节数。保证安全读取。
            buffer.limit(remaining);
            buffer = buffer.slice();
        }
        int nRead = exchanger.doRead(buffer);
        buffer.flip();
        return nRead;
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
            nRead = doRead0();
        }
        //nRead读取的字节不可能超过remaining,必须判断大小，否则buffer剩余字节可能会出错
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
            doRead0();
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
