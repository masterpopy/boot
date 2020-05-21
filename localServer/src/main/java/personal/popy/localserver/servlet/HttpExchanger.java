package personal.popy.localserver.servlet;

import personal.popy.localserver.connect.HttpRequestProtocol;
import personal.popy.localserver.connect.StreamHandler;
import personal.popy.localserver.connect.buffer.ResponseWriter;
import personal.popy.localserver.connect.io.BlockRespWriter;
import personal.popy.localserver.data.StaticBuffer;
import personal.popy.localserver.lifecycle.HttpProcessor;
import personal.popy.localserver.lifecycle.ServerContext;
import personal.popy.localserver.source.Child;
import personal.popy.localserver.wrapper.HttpReqEntity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class HttpExchanger implements StreamHandler<HttpReqEntity>, Runnable {
    private AsynchronousSocketChannel channel;
    private RequestImpl request;
    private ResponseImpl response;
    private ByteBuffer readByteBuffer;


    private HttpRequestProtocol protocol;
    private HttpProcessor processor;

    private ResponseWriter task = new BlockRespWriter();
    private static final byte[] ACK = "HTTP/1.1 100 \r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);

    //public static final Charset DEFAULT_URI_CHARSET = StandardCharsets.UTF_8;
    //public static final Charset DEFAULT_BODY_CHARSET = StandardCharsets.ISO_8859_1;

    public HttpExchanger(HttpProcessor processor, AsynchronousSocketChannel channel) {
        this.processor = processor;
        this.channel = channel;
        this.request = new RequestImpl(this);
        this.protocol = new HttpRequestProtocol(this);
        readByteBuffer = StaticBuffer.allocByteBuffer();
    }


    public void writeAsnycAndSave(ByteBuffer byteBuffer) {
        task.doWrite(this, byteBuffer);
    }

    public ServerContext getServer() {
        return processor.getServer();
    }

    public HttpProcessor getProcessor() {
        return processor;
    }

    @Child
    public HttpRequestProtocol getHttpRequestProtocol() {
        return protocol;
    }


    public RequestImpl getRequest() {
        return request;
    }

    public ResponseImpl createResponse() {
        if (response == null) {
            response = new ResponseImpl(this);
        }
        return response;
    }

    public void realWrite(ByteBuffer byteBuffer) {
        try {
            writeAsnycAndSave(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void commit() {
        //commit
        if (readByteBuffer != null) {
            readByteBuffer.compact();
        }
        request.recycle();
        response = null;
        task.end(this, null);
    }


    @Override
    public void success(HttpReqEntity entity) {
        if (!readByteBuffer.hasRemaining()) {
            StaticBuffer.saveByteBuffer(readByteBuffer);
            readByteBuffer = null;
        }
        //write(ByteBuffer.wrap(ACK));//send ack
        getRequest().doServlet(entity);
    }


    @Override
    public void require(ByteBuffer b, CompletionHandler<Integer, ByteBuffer> c) {
        if (!b.hasRemaining()) {
            //todo
            if (b.capacity() == 1024) {
                ByteBuffer byteBuffer = StaticBuffer.allocByteBuffer8();
                byteBuffer.put(b);
                b = byteBuffer;
            } else {
                createResponse().sendError(304);
                return;
            }

        }
        if (!channel.isOpen()) {
            System.out.println("closed");
            return;
        }
        channel.read(b, 30, TimeUnit.MINUTES, b, c);
    }


    @Override
    public void error() {
        closed();
        System.out.println("error happened");
    }

    @Override
    public void closed() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StaticBuffer.saveByteBuffer(readByteBuffer);
        //System.out.println("closed happened");
    }

    @Override
    public void run() {
        if (channel.isOpen()) {
            if (readByteBuffer == null) {
                readByteBuffer = StaticBuffer.allocByteBuffer();
            }
            getHttpRequestProtocol().asyncParse(readByteBuffer);
        }
    }

    public AsynchronousSocketChannel getChannel() {
        return channel;
    }
}
