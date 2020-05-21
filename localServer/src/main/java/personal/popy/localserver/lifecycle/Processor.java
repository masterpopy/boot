package personal.popy.localserver.lifecycle;

import java.nio.channels.AsynchronousSocketChannel;

public interface Processor {
    void processNewConnection(AsynchronousSocketChannel result);

    void setServer(ServerContext server);
}
