package personal.popy.localserver.io;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AioListener implements AsyncListener {
    private ByteBuffer buffer;

    private int pos;

    private List<AsyncCondition> conditions = new ArrayList<>();

    private AsyncTarget target;



    public void init() {

    }

    private static class ReadToSpaceCondition implements AsyncCondition {

        @Override
        public int meet(AioListener result) {
            ByteBuffer buffer = result.getBuffer();
            int limit = buffer.limit();
            int pos = result.getPos();
            while (pos < limit) {
                byte b = buffer.get(pos++);
                if (b == ' ' || b == '\t') {
                    return pos;
                }
            }
            return -1;
        }
    }


    public int getPos() {
        return pos;
    }

    @Override
    public boolean signal() {
        return false;
    }

    @Override
    public ByteBuffer getBuffer() {
        return buffer;
    }
}
