package personal.popy.record.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;


/**
 * description:
 *
 * @author wkGui
 */
public class ServerHandler {
    private int bufferSize = 1024;
    private String localCharset = "UTF-8";

    public ServerHandler() {
    }

    public ServerHandler(int bufferSize) {
        this(bufferSize, null);
    }

    public ServerHandler(String localCharset) {
        this(-1, localCharset);
    }

    public ServerHandler(int bufferSize, String localCharset) {
        this.bufferSize = bufferSize > 0 ? bufferSize : this.bufferSize;
        this.localCharset = localCharset == null ? this.localCharset : localCharset;
    }

    public void handleAccept(SelectionKey selectionKey) throws IOException {
        //获取channel
        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        //非阻塞
        socketChannel.configureBlocking(false);
        //注册selector
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));

        System.out.println("建立请求......");
    }

    public String handleRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

        String receivedStr = "";

        if (socketChannel.read(buffer) == -1) {
            //没读到内容关闭
            socketChannel.shutdownOutput();
            socketChannel.shutdownInput();
            socketChannel.close();
            throw new IOException("连接断开......");
        } else {
            //将channel改为读取状态
            buffer.flip();
            //按照编码读取数据
            receivedStr = Charset.forName(localCharset).newDecoder().decode(buffer).toString();
            buffer.clear();

            //返回数据给客户端
            buffer = buffer.put(("received string : " + receivedStr).getBytes(localCharset));
            //读取模式
            buffer.flip();
            socketChannel.write(buffer);

            buffer.clear();
            //注册selector 继续读取数据
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, buffer);
        }
        return receivedStr;
    }

}