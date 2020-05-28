package personal.popy.localserver.wrapper;

import personal.popy.localserver.util.UrlDecoder;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class SliencedBuffer {
    private static final ThreadLocal<CharBuffer> tcb = ThreadLocal.withInitial(()->CharBuffer.allocate(1024));

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

    public SliencedBuffer reset(ByteBuffer  origin, int start, int limit) {
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


    public String ansiString(int start, int length) {
        CharBuffer cb = tcb.get();
        char[] array = cb.array();
        for (int i = 0; i < length; i++) {
            array[i] = (char) origin.get(i + start);
        }
        return new String(array, 0, length);
    }

    public String ansiString() {
       return ansiString(0, length);
    }


    public long getLong() {
        int i = length;
        if (i > 8) {
            throw new ArithmeticException("值过大");
        }
        long ret = 0;
        for (int j = 0; j < i; j++) {
            ret = ret << 8;
            ret |= Character.toUpperCase(origin.get(j+start));
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


    public static int parseInt(byte[] s, int beginIndex, int endIndex, int radix) throws NumberFormatException {
        if (beginIndex >= 0 && beginIndex <= endIndex && endIndex <= s.length) {
            if (radix < 2) {
                throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
            } else if (radix > 36) {
                throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
            } else {
                boolean negative = false;
                int i = beginIndex;
                int limit = -2147483647;
                if (beginIndex >= endIndex) {
                    throw new NumberFormatException("");
                } else {
                    byte firstChar = s[beginIndex];
                    if (firstChar < '0') {
                        if (firstChar == '-') {
                            negative = true;
                            limit = -2147483648;
                        } else if (firstChar != '+') {
                            throw new NumberFormatException();
                        }

                        i = beginIndex + 1;
                        if (i == endIndex) {
                            throw new NumberFormatException();
                        }
                    }

                    int multmin = limit / radix;

                    int result;
                    int digit;
                    for (result = 0; i < endIndex; result -= digit) {
                        digit = Character.digit(s[i], radix);
                        if (digit < 0 || result < multmin) {
                            throw new NumberFormatException();
                        }

                        result *= radix;
                        if (result < limit + digit) {
                            throw new NumberFormatException();
                        }
                        ++i;
                    }

                    return negative ? result : -result;
                }
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int getLength() {
        return length;
    }
}
