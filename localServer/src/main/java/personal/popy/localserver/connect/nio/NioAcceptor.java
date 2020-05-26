package personal.popy.localserver.connect.nio;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioAcceptor {
    ServerSocketChannel serverSocket;

    public NioAcceptor(ServerSocketChannel serverSocket) {
        this.serverSocket = serverSocket;
        init();
    }

    private void init() {
        try {
            serverSocket.configureBlocking(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
           SocketChannel channel = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
