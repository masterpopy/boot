package personal.popy.localserver.connect.buffer;

import java.io.IOException;
import java.io.Writer;

public class CharBufferWriter extends Writer {

    private ByteBufferStream bs;

    public CharBufferWriter(ByteBufferStream cb) {
        this.bs = cb;
    }

    @Override
    public void write(int c) throws IOException {
        bs.write((char) c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        bs.write(cbuf, off, len);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        bs.write(str, off, len);
    }


    @Override
    public void flush() {
        bs.flush();
    }

    @Override
    public void close() {

    }

}
