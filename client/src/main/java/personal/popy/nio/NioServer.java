package personal.popy.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioServer {

	// 通道管理器
	private Selector selector;

	public NioServer(int port) {
		try {
			startAccept(port);
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public NioServer(){
		this(8080);
	}

	void startAccept(int port) throws IOException {
		ServerSocketChannel acceptorSvr = ServerSocketChannel.open();
		acceptorSvr.socket().bind(new InetSocketAddress(port));
		acceptorSvr.configureBlocking(false);

		selector = Selector.open();

		acceptorSvr.register(selector, SelectionKey.OP_ACCEPT);
	}

	void listen() throws IOException {
		// 轮询访问selector
		while (true) {
			// 当注册的事件到达时，方法返回；否则,该方法会一直阻塞,此方法是线程安全的
			selector.select();
			// 获得selector中选中的项的迭代器，选中的项为注册的事件
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while (ite.hasNext()) {
				SelectionKey key = ite.next();
				// 删除已选的key,以防重复处理
				ite.remove();

				if (key.isAcceptable()) {// 客户端请求连接事件
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					// 获得和客户端连接的通道
					SocketChannel channel = server.accept();
					// 设置成非阻塞
					channel.configureBlocking(false);

					// 在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
					channel.register(this.selector, SelectionKey.OP_READ);

				} else if (key.isReadable()) {// 获得了可读的事件
					read(key);
				}

			}
		}
	}

	void read(SelectionKey key) throws IOException {
		// 服务器可读取消息:得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(512);
		int count = channel.read(buffer);
		if(count <= 0) return;
 		byte[] data = buffer.array();
		String msg = new String(data, 0, count);

		channel.write(write(msg));// 将消息回送给客户端

	}

	static ByteBuffer write(String msg) {
		//返回客户端
		StringBuilder header = new StringBuilder();

		header.append("Http/1.1 200 Ok\r\n");

		String localCharSet = "UTF-8";
		header.append("Content-Type:text/html;charset=").append(localCharSet).append("\r\n");



		StringBuilder body = new StringBuilder();

		body.append("<html><head><title>显示报文</title></head><body>");

		body.append("接受到请求的报文是：+<br>");

		for (String s : msg.split("\r\n")) {

			body.append(s).append("<br/>");

		}
		body.append("</body></html>");
		String data = body.toString();
		byte[] byteData = data.getBytes();
		header.append("Content-Length:").append(byteData.length).append("\r\n");
		header.append("\r\n");


		return (ByteBuffer) ByteBuffer.allocate(1024*2).put(header.toString().getBytes()).put(byteData).flip();
	}
}

