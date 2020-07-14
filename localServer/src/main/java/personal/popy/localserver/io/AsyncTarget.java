package personal.popy.localserver.io;

import java.nio.ByteBuffer;

interface AsyncTarget {
    void set(ByteBuffer buffer, int start, int end);
}
