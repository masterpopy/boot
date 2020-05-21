package personal.popy.localserver.util;

import java.nio.ByteBuffer;

public class BufferUtil {
    public static void put(ByteBuffer writeByteBuffer, String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            writeByteBuffer.put((byte) str.charAt(i));
        }
    }
}
