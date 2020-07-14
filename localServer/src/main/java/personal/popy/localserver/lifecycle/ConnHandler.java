package personal.popy.localserver.lifecycle;

import java.nio.channels.AsynchronousSocketChannel;

public interface ConnHandler {
    void processNewConnection(AsynchronousSocketChannel result, ConnectionContext connectionContext);

    void setServer(ServerContext server);
}
