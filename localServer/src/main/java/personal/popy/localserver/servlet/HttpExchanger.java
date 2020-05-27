package personal.popy.localserver.servlet;

import personal.popy.localserver.connect.StreamHandler;
import personal.popy.localserver.connect.buffer.ResponseWriter;
import personal.popy.localserver.data.ProcessBuffer;
import personal.popy.localserver.lifecycle.HttpProcessor;
import personal.popy.localserver.lifecycle.ServerContext;
import personal.popy.localserver.source.Child;
import personal.popy.localserver.util.TimeMonitor;
import personal.popy.localserver.wrapper.HttpReqEntity;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpExchanger extends TimeMonitor implements StreamHandler<HttpReqEntity>, Runnable {
    private AsynchronousSocketChannel channel;
    private RequestImpl request;
    private ResponseImpl response;


    private ProcessBuffer buf;
    private ByteBuffer readBuf;

    private HttpRequestProtocol protocol;
    private HttpProcessor processor;

    private ResponseWriter task = ResponseWriter.newBlock();
//    private static final byte[] ACK = "HTTP/1.1 100 \r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);

    //public static final Charset DEFAULT_URI_CHARSET = StandardCharsets.UTF_8;
    //public static final Charset DEFAULT_BODY_CHARSET = StandardCharsets.ISO_8859_1;
    public static final AtomicInteger suc = new AtomicInteger(1);

    public HttpExchanger(HttpProcessor processor, AsynchronousSocketChannel channel) {
        this.processor = processor;
        this.channel = channel;
        this.request = new RequestImpl(this);
        this.protocol = new HttpRequestProtocol(this);
        this.readBuf = ByteBuffer.allocateDirect(1024);
    }

    public ResponseWriter getTask() {
        return task;
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
            task.doWrite(this, byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void commit() {
        //commit
        suc.incrementAndGet();
        if (readBuf.hasRemaining()) {
            readBuf.compact();
        } else {
            readBuf.clear();
        }
        request.recycle();
        response = null;


        task.end(this, null);
    }


    @Override
    public void success(HttpReqEntity entity) {
        if (buf == null) {
            buf = ProcessBuffer.alloc();
        }
        //write(ByteBuffer.wrap(ACK));//send ack
        super.timeEnd();
        getRequest().doServlet(entity);
    }


    @Override
    public void require(ByteBuffer b, CompletionHandler<Integer, ByteBuffer> c) {
        //todo buf size
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
        channel = null;
        end();
        getProcessor().endRequest(this);
        //System.out.println("closed happened");
    }

    @Override
    public void run() {
        doParse();
    }

    public void doParse() {
        if (channel.isOpen()) {
            getHttpRequestProtocol().asyncParse(readBuf);
        }
    }


    public AsynchronousSocketChannel getChannel() {
        return channel;
    }

    public ProcessBuffer getBuf() {
        return buf;
    }

    public void end() {
        if (buf != null) {
            buf.save();
            buf = null;
        }
    }

    public void setChannel(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }
}
