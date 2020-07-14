package personal.popy.localserver.io.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NioSelector implements NioHandler {
    private Selector readOrAccept;
    private ServerSocketChannel serverChannel;

    private Executor[] singles;

    private int cnt;

    public void init() {
        try {
            readOrAccept = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.register(readOrAccept, SelectionKey.OP_ACCEPT, this);
            singles = new Executor[16];
            for (int i = 0; i < 16; i++) {
                singles[i] = Executors.newSingleThreadExecutor();
            }
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
            NioDispacher att = new NioDispacher(accept);
            att.setExecutor(singles[cnt++ & 15]);
            accept.register(readOrAccept, SelectionKey.OP_READ, att);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
