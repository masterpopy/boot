package personal.popy.localserver.io;

import java.nio.ByteBuffer;

public class ByteBufReader {
    private int start;

    private int pos;

    private ByteBuffer buf;

    public void setBuf(ByteBuffer buf) {
        this.buf = buf;
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return buf.limit();
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public byte get(int idx) {
        return buf.get(idx);
    }

    public void read() {
        if (start > 0) {
            buf.position(start);
            buf.compact();
            pos = pos - start;
            start = 0;
        }
    }

    public void skip(int count) {
        start += count;
    }

}
