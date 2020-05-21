package personal.popy.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

	public BioServer() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void init() throws IOException {
		ServerSocket socket = new ServerSocket(8080);
		Socket socket1 = socket.accept();
		InputStream is = socket1.getInputStream();
		InputStreamReader br = new InputStreamReader(is);
		char[] ch=new char[1024];
		while (true) {
			int r= br.read(ch);
			if(r == -1)break;
			System.out.print(new String(ch,0,r));
		}
		//http协议请求体和请求头以空行结束分割
		socket1.close();
		socket.close();
	}
}
