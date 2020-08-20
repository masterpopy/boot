package personal.popy.localserver.lifecycle;

import java.nio.channels.AsynchronousSocketChannel;

public interface ConnHandler {
    void processNewConnection(AsynchronousSocketChannel result);
}
