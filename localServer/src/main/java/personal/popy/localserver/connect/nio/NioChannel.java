package personal.popy.localserver.connect.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioChannel {
    Selector selector;


    public <A> void read(SocketChannel socketChannel, ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) throws ClosedChannelException {
        socketChannel.register(selector, SelectionKey.OP_READ, new Attachment<>(attachment, dst, handler));
    }

    private static class Attachment<A>{
        A attachment;
        ByteBuffer dst;
        CompletionHandler<Integer, ? super A> handler;

        public Attachment(A attachment, ByteBuffer dst, CompletionHandler<Integer, ? super A> handler) {
            this.attachment = attachment;
            this.dst = dst;
            this.handler = handler;
        }
    }

    public void run() {
        try {
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (SelectionKey selectedKey : selector.selectedKeys()) {
            Attachment<? super Object> attachment = (Attachment<? super Object>) selectedKey.attachment();
            SocketChannel channel = (SocketChannel) selectedKey.channel();
            try {
                int read = channel.read(attachment.dst);
                attachment.handler.completed(read, attachment.attachment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
