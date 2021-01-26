import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public interface IoStream {

    int read(ByteBuffer b);

    void write(ByteBuffer b);

    void read(ByteBuffer b, Master master, CompletionHandler<Integer, Master> handler);

    void close();
}
