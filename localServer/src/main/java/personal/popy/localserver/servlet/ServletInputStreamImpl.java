package personal.popy.localserver.servlet;

import personal.popy.localserver.io.stream.LengthReader;
import personal.popy.localserver.io.stream.RequestReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;

//这个类并不安全。
public class ServletInputStreamImpl extends ServletInputStream {
    private RequestReader reader;


    public ServletInputStreamImpl(RequestImpl request) {
        reader = new LengthReader(request.getContentLength(), request.getExchanger());
    }

    @Override
    public boolean isFinished() {
        return reader.hasRemaining();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return reader.doRead();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return reader.doRead(b, off, len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
