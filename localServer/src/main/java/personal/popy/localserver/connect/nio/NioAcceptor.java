package personal.popy.localserver.connect.nio;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioAcceptor {
    ServerSocketChannel serverSocket;

    public void run() {
        SocketChannel channel;
        try {
            channel = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
