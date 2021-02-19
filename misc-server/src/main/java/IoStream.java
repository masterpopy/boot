import java.nio.ByteBuffer;
import java.util.function.Consumer;

public interface IoStream {

    void read(ByteBuffer b, Consumer<Integer> handler);

    void write(ByteBuffer b);

    void close();
}
