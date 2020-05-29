package personal.popy.localserver.connect.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NioSelector implements NioHandler {
    private Selector readOrAccept;
    private ServerSocketChannel serverChannel;


    public void init() {
        try {
            readOrAccept = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.register(readOrAccept, SelectionKey.OP_ACCEPT, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            readOrAccept.select();
            Set<SelectionKey> selectionKeys = readOrAccept.selectedKeys();
            for (SelectionKey key : selectionKeys) {
                ((NioHandler) key.attachment()).handle(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handle(SelectionKey key) {
        try {
            SocketChannel accept = serverChannel.accept();
            accept.configureBlocking(false);
            accept.register(readOrAccept, SelectionKey.OP_READ, new NioReader(accept));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
