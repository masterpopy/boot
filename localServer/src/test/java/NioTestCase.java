import org.junit.Test;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioTestCase {
    //java nio是水平触发
    @Test
    public void nioTest() throws Exception {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(8080));
        channel.configureBlocking(true);
        new Thread(()->{
            try {
                bioSend();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        SocketChannel accept = channel.accept();
        Selector s = Selector.open();

        accept.configureBlocking(false);
        SelectionKey register = accept.register(s, SelectionKey.OP_READ);

        s.select();
        s.selectedKeys().clear();
        System.out.println(s.select());
        ByteBuffer b = ByteBuffer.allocate(1024);

        accept.read(b);
        System.out.println(accept.read(b));

        System.out.println(new String(b.array(), 0, b.position()));


        s.select();


        accept.read(b);

        System.out.println(new String(b.array(), 0, b.position()));

    }

    @Test
    public void bioSend() throws Exception {
        Socket localhost = new Socket("localhost", 8080);

        OutputStream outputStream = localhost.getOutputStream();

        outputStream.write("123".getBytes());
        outputStream.flush();
        Thread.sleep(1000);

        outputStream.write("456".getBytes());
        outputStream.flush();
        outputStream.close();
        localhost.close();
    }
}
