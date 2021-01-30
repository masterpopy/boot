import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

public interface IoStream {

    int read(ByteBuffer b, Consumer<Integer> handler);

    void write(ByteBuffer b);

    void close();
}
