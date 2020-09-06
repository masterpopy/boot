package personal.popy.server.io;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class ReusableBufferedInputStream extends BufferedInputStream {
    public ReusableBufferedInputStream(InputStream in) {
        super(in);
    }

    public ReusableBufferedInputStream(InputStream in, int size) {
        super(in, size);
    }

    public void setIn(InputStream in) {
        pos = marklimit = count = 0;
        this.in = in;
    }
}
