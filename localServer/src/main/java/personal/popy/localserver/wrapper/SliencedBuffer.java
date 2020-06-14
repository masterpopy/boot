package personal.popy.localserver.wrapper;

import personal.popy.localserver.util.UrlDecoder;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class SliencedBuffer {
    private static final ThreadLocal<CharBuffer> tcb = ThreadLocal.withInitial(() -> CharBuffer.allocate(1024));

    public SliencedBuffer() {
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    public ByteBuffer getOrigin() {
        return origin;
    }

    public SliencedBuffer reset(ByteBuffer origin, int start, int limit) {
        this.start = start;
        this.limit = limit;
        this.origin = origin;
        length = limit - start;
        return this;
    }

    private int start;
    private int limit;
    private int length;
    private ByteBuffer origin;


    public String stringValue(int start, int length) {
        CharBuffer cb = tcb.get();
        char[] array = cb.array();
        for (int i = 0; i < length; i++) {
            array[i] = (char) origin.get(i + start);
        }
        return new String(array, 0, length);
    }

    public String stringValue() {
        return stringValue(0, length);
    }


    public long getLong() {
        int i = length;
        if (i > 8) {
            throw new ArithmeticException("值过大");
        }
        long ret = 0;
        for (int j = 0; j < i; j++) {
            ret = ret << 8;
            ret |= Character.toUpperCase(origin.get(j + start));
        }
        return ret;
    }

    public void decodeUrl(HttpReqEntity en) {
        CharBuffer cb = tcb.get();
        //todo charset
        while (start < limit) {
            byte b = origin.get(start);
            if (b == '?') {
                break;
            }
            cb.put((char) b);
            start++;
        }
        en.uri = new String(cb.array(), 0, cb.position());
        cb.clear();
        if (start == limit) {
            return;
        }
        start++;//skip ?
        if (limit - start > 1) { // at least two char
            UrlDecoder.convert(this, cb, StandardCharsets.UTF_8.newDecoder(), en);
        }
        cb.clear();

    }


    public int getLength() {
        return length;
    }
}
