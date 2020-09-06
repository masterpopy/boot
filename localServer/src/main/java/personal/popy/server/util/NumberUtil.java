package personal.popy.server.util;

public class NumberUtil {
    private static final byte[] DigitTens = new byte[]{48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57};
    private static final byte[] DigitOnes = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};

    public static String toString(int i) {
        int size = stringSize(i);
        byte[] buf;
        buf = new byte[size];
        getChars(i, size, buf);
        return new String(buf);
    }

    public static String toString(long i) {
        int size = stringSize(i);
        byte[] buf;
        buf = new byte[size];
        getChars(i, size, buf);
        return new String(buf);
    }


    static int stringSize(long x) {
        int d = 1;
        if (x >= 0L) {
            d = 0;
            x = -x;
        }

        long p = -10L;

        for(int i = 1; i < 19; ++i) {
            if (x > p) {
                return i + d;
            }

            p = 10L * p;
        }

        return 19 + d;
    }

    static int stringSize(int x) {
        int d = 1;
        if (x >= 0) {
            d = 0;
            x = -x;
        }

        int p = -10;

        for (int i = 1; i < 10; ++i) {
            if (x > p) {
                return i + d;
            }

            p = 10 * p;
        }

        return 10 + d;
    }

    static int getChars(long i, int index, byte[] buf) {
        int charPos = index;
        boolean negative = i < 0L;
        if (!negative) {
            i = -i;
        }

        int r;
        while(i <= -2147483648L) {
            long q = i / 100L;
            r = (int)(q * 100L - i);
            i = q;
            --charPos;
            buf[charPos] = DigitOnes[r];
            --charPos;
            buf[charPos] = DigitTens[r];
        }

        int q2;
        int i2;
        for(i2 = (int)i; i2 <= -100; buf[charPos] = DigitTens[r]) {
            q2 = i2 / 100;
            r = q2 * 100 - i2;
            i2 = q2;
            --charPos;
            buf[charPos] = DigitOnes[r];
            --charPos;
        }

        q2 = i2 / 10;
        r = q2 * 10 - i2;
        --charPos;
        buf[charPos] = (byte)(48 + r);
        if (q2 < 0) {
            --charPos;
            buf[charPos] = (byte)(48 - q2);
        }

        if (negative) {
            --charPos;
            buf[charPos] = 45;
        }

        return charPos;
    }

    static int getChars(int i, int index, byte[] buf) {
        int charPos = index;
        boolean negative = i < 0;
        if (!negative) {
            i = -i;
        }

        int q;
        int r;
        while (i <= -100) {
            q = i / 100;
            r = q * 100 - i;
            i = q;
            --charPos;
            buf[charPos] = DigitOnes[r];
            --charPos;
            buf[charPos] = DigitTens[r];
        }

        q = i / 10;
        r = q * 10 - i;
        --charPos;
        buf[charPos] = (byte) (48 + r);
        if (q < 0) {
            --charPos;
            buf[charPos] = (byte) (48 - q);
        }

        if (negative) {
            --charPos;
            buf[charPos] = 45;
        }

        return charPos;
    }
}
