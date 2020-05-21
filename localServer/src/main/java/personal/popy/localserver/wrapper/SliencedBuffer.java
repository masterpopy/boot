package personal.popy.localserver.wrapper;

import personal.popy.localserver.data.StaticBuffer;
import personal.popy.localserver.util.UrlDecoder;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class SliencedBuffer implements Cloneable {

    public SliencedBuffer(byte[] origin, int start, int limit) {
        reset(origin, start, limit);
    }

    public SliencedBuffer() {
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    public byte[] getOrigin() {
        return origin;
    }

    public SliencedBuffer reset(byte[] origin, int start, int limit) {
        this.start = start;
        this.limit = limit;
        this.origin = origin;
        length = limit - start;
        stringValue = null;
        return this;
    }

    private int start;
    private int limit;
    private int length;
    private byte[] origin;
    private String stringValue;


    public String toString() {
        if (origin == null) return "";
        if (stringValue != null) {
            return stringValue;
        }
        return stringValue = new String(origin, start, length);
    }

    public String ansiString(int start, int length) {
        CharBuffer cb = StaticBuffer.allocCharBuffer();
        char[] array = cb.array();
        for (int i = 0; i < length; i++) {
            array[i] = (char) origin[i + start];
        }
        String s = new String(array, 0, length);
        StaticBuffer.saveCharBuffer(cb);
        return s;
    }

    public String ansiString() {
       return ansiString(0, length);
    }

    public static int hashCode(byte[] value, int start, int length) {
        int h = 0;
        for (int i = 0; i < length; ++i) {
            byte v = value[i + start];
            h = 31 * h + (v & 255);
        }

        return h;
    }

    public long getLong() {
        int i = length;
        if (i > 8) {
            throw new ArithmeticException("值过大");
        }
        long ret = 0;
        for (int j = 0; j < i; j++) {
            ret = ret << 8;
            ret |= Character.toUpperCase(origin[j + start]);
        }
        return ret;
    }

    public SliencedBuffer setStart(int start) {
        this.start = start;
        stringValue = null;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() == o.getClass()) {
            SliencedBuffer that = (SliencedBuffer) o;
            if (length != that.length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (origin[start + i] != that.origin[that.start + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public boolean equalsIgnoreCase(String that) {
        if (that == null) return false;
        //unsafe, this string must be ansi, but headers can only be ansi, parameters should not use this method
        if (stringValue != null) {
            return stringValue.equals(that);
        }
        for (int i = 0; i < length; i++) {
            if (Character.toLowerCase(origin[start + i]) != Character.toLowerCase(that.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int hashCode() {
        if (stringValue != null) {
            return stringValue.hashCode();
        }
        return hashCode(origin, start, length);
    }

    public void decodeUrl(HttpReqEntity en) {
        CharBuffer cb = StaticBuffer.allocCharBuffer();
        //todo charset
        while (start < limit) {
            byte b = origin[start];
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
        StaticBuffer.saveCharBuffer(cb);

    }


    public int getInt() {
        return parseInt(origin, start, limit, 10);
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
