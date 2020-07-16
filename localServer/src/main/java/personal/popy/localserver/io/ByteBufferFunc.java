package personal.popy.localserver.io;

import java.nio.ByteBuffer;

public class ByteBufferFunc {

    public boolean readToSpace(ByteBuffer buffer) {
        int start = buffer.position();
        int end = buffer.limit();

        while (start < end) {
            int b = buffer.get(start++);

            if (b == ' ' || b == '\t') {
                buffer.position(start);
                return true;
            }
        }

        return false;
    }
}
