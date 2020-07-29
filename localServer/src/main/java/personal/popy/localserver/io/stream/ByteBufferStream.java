package personal.popy.localserver.io.stream;

import personal.popy.localserver.servlet.ResponseImpl;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class ByteBufferStream extends ServletOutputStream {

    private ResponseImpl response;
    private ByteBuffer ob;
    private CharBuffer cb;

    private CharsetEncoder encoder;

    private boolean isReady = true;
    private WriteListener writeListener;

    public ByteBufferStream(ResponseImpl response) {
        this.response = response;
        ob = response.getHttpExchanger().getBuf().getStreamBuf();
    }


    private void enableCharBuffer() {
        if (cb == null) {
            cb = response.getHttpExchanger().getBuf().getCharBuf();
        }
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        if (writeListener == null) {
            throw new NullPointerException("response.writeListenerSet");
        }
        if (this.writeListener != null) {
            throw new IllegalStateException("response.writeListenerSet");
        }
        this.writeListener = writeListener;
    }

    @Override
    public void write(int b) {
        ob.put((byte) b);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        while (len > 0) {
            int writebytes;
            if (len > ob.remaining()) {
                writebytes = ob.remaining();
                ob.put(b, off, writebytes);
                flush();
            } else {
                writebytes = len;
                ob.put(b, off, writebytes);
            }
            len -= writebytes;
            off += writebytes;
        }
    }

    void write(char s) throws IOException {
        enableCharBuffer();
        cb.put(s);
        encode();
    }

    void write(char[] s, int off, int len) throws IOException {
        enableCharBuffer();
        while (len > 0) {
            int writebytes = Math.min(len, cb.capacity());
            cb.put(s, off, writebytes);
            encode();
            len -= cb.capacity();
            off += writebytes;
        }
    }

    void write(String s, int off, int len) throws IOException {
        enableCharBuffer();
        while (len > 0) {
            int capacity = cb.capacity();
            int writebytes = Math.min(len, capacity);
            cb.put(s, off, writebytes);
            encode();
            len -= capacity;
            off += writebytes;
        }
    }

    private void encode() throws IOException {
        if (encoder == null) {
            encoder = response.getCharset().newEncoder()
                    .onUnmappableCharacter(CodingErrorAction.REPORT).onMalformedInput(CodingErrorAction.REPORT);
        }
        cb.flip();
        CoderResult encode;
        do {
            encode = encoder.encode(cb, ob, false);
            if (encode.isError()) {
                encode.throwException();
            }
        } while (judgeResult(encode));
        cb.clear();
    }

    private boolean judgeResult(CoderResult coderResult) throws IOException {
        if (coderResult.isOverflow()) {
            flush();
            return true;
        } else if (coderResult.isUnderflow()) {
            //todo lifted char
            return false;
        }
        return false;
    }


    @Override
    public void flush() {
        response.sendBody();
        ob.clear();
    }

    @Override
    public void close() {
        flush();
        cb = null;
        ob = null;
        response = null;
    }

    public CharBuffer getCb() {
        return cb;
    }

    public ByteBuffer getOb() {
        return ob;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public WriteListener getWriteListener() {
        return writeListener;
    }
}
