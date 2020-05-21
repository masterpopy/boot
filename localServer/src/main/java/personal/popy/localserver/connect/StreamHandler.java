package personal.popy.localserver.connect;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public interface StreamHandler<T> {
    void success(T t);

    void require(ByteBuffer b, CompletionHandler<Integer, ByteBuffer> c);

    void error();

    void closed();
}
