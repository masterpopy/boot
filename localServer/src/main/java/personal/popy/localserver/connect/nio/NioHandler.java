package personal.popy.localserver.connect.nio;

import java.nio.channels.SelectionKey;

public interface NioHandler {
    void handle(SelectionKey key);
}
