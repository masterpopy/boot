package personal.popy.localserver.util;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;

@SuppressWarnings("UnusedReturnValue")
public class UnSafeStrBuf {
    private char[] buf;

    private int pos;

    private int max;
    private final static char[] DigitTens = a.DigitTens;

    private final static char[] DigitOnes = a.DigitOnes;

    private final static char[] digits = a.digits;

    private final static int[] sizeTable = a.sizeTable;

    private static class a {
        private static char[] DigitTens;

        private static char[] DigitOnes;

        private static char[] digits;

        private static int[] sizeTable;

        static {
            //steal from Integer
            try {
                Field[] declaredFields = Integer.class.getDeclaredFields();
                for (Field field : declaredFields) {
                    String name = field.getName();
                    field.setAccessible(true);
                    if ("DigitTens".equals(name)) {
                        DigitTens = (char[]) field.get(null);
                    } else if ("DigitOnes".equals(name)) {
                        DigitOnes = (char[]) field.get(null);
                    } else if ("digits".equals(name)) {
                        digits = (char[]) field.get(null);
                    } else if ("sizeTable".equals(name)) {
                        sizeTable = (int[]) field.get(null);
                    }
                }
            } catch (Throwable ignored) {

            }
        }
    }

    public UnSafeStrBuf(int cnt) {
        buf = new char[cnt];
    }

    public UnSafeStrBuf() {
        this(16);
        max = Integer.MAX_VALUE - 8;
    }

    public UnSafeStrBuf(char[] b) {
        buf = b;
        max = b.length;
    }

    private void ensureCount(int newLength) {
        if (newLength >= buf.length) {
            buf = Arrays.copyOf(buf, newCapacity(newLength));
        }
    }

    private int newCapacity(int minCapacity) {
        // overflow-conscious code
        int newCapacity = (buf.length << 1) + 2;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        return (newCapacity <= 0 || max - newCapacity < 0)
                ? hugeCapacity(minCapacity)
                : newCapacity;
    }

    private int hugeCapacity(int minCapacity) {
        if (max - minCapacity < 0) { // overflow
            throw new IndexOutOfBoundsException("超过缓存最大值：" + max);
        }
        return Math.max(minCapacity, max);
    }

    public UnSafeStrBuf append(char c) {
        ensureCount(1);
        buf[pos++] = c;
        return this;
    }

    public UnSafeStrBuf append(char[] c) {
        if (c == null) {
            append("Null");
            return this;
        }
        ensureCount(c.length + pos);
        System.arraycopy(c, 0, buf, pos, c.length);
        pos += c.length;
        return this;
    }

    public UnSafeStrBuf append(String s) {
        if (s == null) {
            s = "Null";
        }
        int length = s.length();
        ensureCount(length + pos);
        s.getChars(0, length, buf, pos);
        pos += length;
        return this;
    }

    public UnSafeStrBuf append(int i) {
        if (i == Integer.MIN_VALUE) {
            append("-2147483648");
            return this;
        }
        int appendedLength = (i < 0) ? stringSize(-i) + 1
                : stringSize(i);
        int spaceNeeded = pos + appendedLength;
        ensureCount(spaceNeeded);
        getChars(i, spaceNeeded, buf);
        pos += appendedLength;
        return this;
    }

    public void writeTo(Writer writer) throws IOException {
        writer.write(buf, 0, pos);
    }

    public static void intGet(int i, int index, char[] buf) {
        if (i == Integer.MIN_VALUE) {
            "-2147483648".getChars(0, 11, buf, index);
            return;
        }
        int appendedLength = (i < 0) ? stringSize(-i) + 1 : stringSize(i);
        if (appendedLength + index > buf.length) {
            throw new IndexOutOfBoundsException();
        }
        getChars(i, index + appendedLength, buf);
    }


    /**
     * Places characters representing the integer i into the
     * character array buf. The characters are placed into
     * the buffer backwards starting with the least significant
     * digit at the specified index (exclusive), and working
     * backwards from there.
     * <p>
     * Will fail if i == Integer.MIN_VALUE
     */
    private static void getChars(int i, int index, char[] buf) {
        int q, r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Generate two digits per iteration
        while (i >= 65536) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = i - ((q << 6) + (q << 5) + (q << 2));
            i = q;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (; ; ) {
            q = (i * 52429) >>> (16 + 3);
            r = i - ((q << 3) + (q << 1));  // r = i-(q*10) ...
            buf[--charPos] = digits[r];
            i = q;
            if (i == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }


    // Requires positive x
    private static int stringSize(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    public String getAndReset() {
        String b = toString();
        pos = 0;
        return b;
    }

    @Override
    public String toString() {
        return new String(buf, 0, pos);
    }

    public char[] getBuf() {
        return buf;
    }

    public void clear() {
        pos = 0;
    }
}
