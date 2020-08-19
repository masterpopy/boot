package personal.popy.localserver.util;

import java.nio.ByteBuffer;

public class BufferUtil {
    public static void put(ByteBuffer wb, String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            wb.put((byte) str.charAt(i));
        }
    }
}
