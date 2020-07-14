package personal.popy.localserver.io.nio;

import java.nio.channels.SelectionKey;

public interface NioHandler {
    void handle(SelectionKey key);
}
