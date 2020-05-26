package personal.popy.localserver.lifecycle;

import java.nio.channels.AsynchronousSocketChannel;

public interface Processor {
    void processNewConnection(AsynchronousSocketChannel result, ConnectionContext connectionContext);

    void setServer(ServerContext server);
}
