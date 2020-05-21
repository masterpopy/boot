package personal.popy.record.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import static java.nio.channels.SelectionKey.*;

public class NioSocketServer {

    public int flag;
    public int port;

    public NioSocketServer(int port) {
        this.port = port;
    }

    public void start() {

        //创建serverSocketChannel，监听8888端口
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //为serverChannel注册selector
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务端开始工作：");

            //创建消息处理器
            ServerHandler handler = new ServerHandler();

            while (flag == 0) {
                selector.select(1000);
                //获取selectionKeys并处理
                for (SelectionKey key : selector.selectedKeys()) {
                    try {
                        //连接请求
                        switch (key.readyOps()) {
                            default:
                                throw new IOException("invalid readyOps");
                            case OP_READ:
                                System.out.println(handler.handleRead(key));
                                break;
                            case OP_ACCEPT:
                                handler.handleAccept(key);
                                break;
                            case OP_WRITE:
                                break;
                            case OP_CONNECT:
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        flag = 1;
                    }
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
