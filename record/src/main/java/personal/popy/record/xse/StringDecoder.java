package personal.popy.record.xse;

import java.io.InputStream;

public class StringDecoder {


    private static byte[] DECODE;
    private static final int D_START = 512;
    private static final int D_END = 15548;

    static {
        DECODE = new byte[D_END - D_START];

        try {
            InputStream in = StringDecoder.class.getClassLoader().getResourceAsStream("decode.bin");
            if (in != null) {
                in.read(DECODE);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String decode(byte[] value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            byte v = value[i];
            if (v >= 1 && v <= 0x1e) {
                int index = v << 8 | value[++i];
                index = index * 2 - D_START;
                char c = (char) (DECODE[index] << 8 | DECODE[index + 1]);
                sb.append(c);

            } else {
                sb.append(En.decode(v));
            }
        }
        return sb.toString();
    }
}
