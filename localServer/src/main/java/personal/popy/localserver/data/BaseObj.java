package personal.popy.localserver.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public abstract class BaseObj {
    public abstract void getString(CharBuffer charBuffer);

    public abstract void getBytes(ByteBuffer byteBuffer);
}
