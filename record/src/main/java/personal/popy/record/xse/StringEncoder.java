package personal.popy.record.xse;

import java.io.InputStream;

public class StringEncoder {
    private static byte[] ENCODE;
    private static final int E_START = 39936;
    private static final int E_END   = 81729;


    static {
        try {
            InputStream in = StringEncoder.class.getClassLoader().getResourceAsStream("encode.bin");
            if (in != null) {
                ENCODE = new byte[E_END - E_START];
                in.read(ENCODE);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void encode(char[] data, ArrayBuffer outs) {
        byte[] s = outs.bytes;
        int index = 0;
        for (char c : data) {
            //4E00-9FA5
            if (c >= 0x4e00 && c <= 0x9fa5) {
                int i = c * 2  - E_START;
                byte hi = ENCODE[i];
                byte lo = ENCODE[i + 1];
                s[index++] = hi;
                s[index++] = lo;
            } else {
                byte b = En.encode(c);
                s[index++] = b;
            }
        }
        outs.byteIndex = index;
    }
}
