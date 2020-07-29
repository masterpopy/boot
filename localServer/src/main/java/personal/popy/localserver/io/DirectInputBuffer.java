package personal.popy.localserver.io;

import java.nio.ByteBuffer;

class DirectInputBuffer extends InputBuffer {
    DirectInputBuffer(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    protected void construct() {
        offset = 0;
    }

    @Override
    public byte get(int idx) {
        return buffer.get();
    }

    @Override
    public void compact() {
        buffer.flip().position(offset);
        buffer.compact();
    }

    @Override
    public byte[] array() {
        throw new UnsupportedOperationException();
    }
}
