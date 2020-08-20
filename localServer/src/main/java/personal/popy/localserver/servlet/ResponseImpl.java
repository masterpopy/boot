package personal.popy.localserver.servlet;

import personal.popy.localserver.exception.ServerException;
import personal.popy.localserver.io.stream.ByteBufferStream;
import personal.popy.localserver.io.stream.CharBufferWriter;
import personal.popy.localserver.io.stream.Chunked;
import personal.popy.localserver.io.stream.LengthWriter;
import personal.popy.localserver.io.stream.ResponseWriter;
import personal.popy.localserver.util.BufferUtil;
import personal.popy.localserver.wrapper.HttpRespEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Locale;

public class ResponseImpl implements HttpServletResponse {

    private PrintWriter printWriter;

    private boolean committed;
    private boolean headerSended;

    private ByteBufferStream outputStream;
    private ResponseWriter responseWriter;

    private HttpExchanger exchanger;
    private HttpRespEntity entity;

    ResponseImpl(HttpExchanger exchanger) {

        this.exchanger = exchanger;
        this.entity = new HttpRespEntity();
    }


    public HttpExchanger getHttpExchanger() {
        checkCommitted();
        return exchanger;
    }

    public HttpRespEntity getHttpRespEntity() {
        checkCommitted();
        return entity;
    }

    public void doResponse() {
        checkCommitted();
        run();
    }

    public Charset getCharset() {
        Charset charset = getHttpRespEntity().charset;
        return charset == null ? StandardCharsets.ISO_8859_1 : charset;
    }

    @Override
    public void addCookie(Cookie cookie) {
        checkCommitted();
    }

    @Override
    public boolean containsHeader(String name) {
        checkCommitted();
        return getHttpRespEntity().headers.get(name) != null;
    }

    @Override
    public String encodeURL(String url) {
        return url;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return url;
    }

    @Override
    public String encodeUrl(String url) {
        return url;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return url;
    }

    @Override
    public void sendError(int sc, String msg) {
        checkCommitted();
        getHttpRespEntity().status = sc;

    }

    @Override
    public void sendError(int sc) {
        checkCommitted();
        getHttpRespEntity().status = sc;
    }

    @Override
    public void sendRedirect(String location) {
        checkCommitted();
    }

    @Override
    public void setDateHeader(String name, long date) {
        checkCommitted();
    }

    @Override
    public void addDateHeader(String name, long date) {
        checkCommitted();
    }

    private boolean checkSpecialHeader(String name, String value) {
        char c = name.charAt(0);
        if (c == 'c' || c == 'C') {
            if ("content-length".equalsIgnoreCase(name)) {
                setContentLengthLong(Long.parseLong(value));
                return true;
            }
            if ("content-type".equalsIgnoreCase(name)) {
                int i = value.indexOf(';');
                if (i >= 0) {
                    setCharacterEncoding(value.substring(value.indexOf('=') + 1));
                    setContentType(value.substring(0, i));
                } else {
                    setContentType(value);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void setHeader(String name, String value) {
        checkCommitted();
        if (checkSpecialHeader(name, value)) {
            return;
        }
        getHttpRespEntity().headers.addIfNotExits(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        checkCommitted();
        if (checkSpecialHeader(name, value)) {
            return;
        }
        getHttpRespEntity().headers.add(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        checkCommitted();
    }

    @Override
    public void addIntHeader(String name, int value) {
        checkCommitted();
    }

    @Override
    public void setStatus(int sc) {
        checkCommitted();
        getHttpRespEntity().status = sc;
    }

    @Override
    public void setStatus(int sc, String sm) {
        checkCommitted();
        HttpRespEntity httpRespEntity = getHttpRespEntity();
        httpRespEntity.status = sc;
        httpRespEntity.msg = sm;
    }

    @Override
    public int getStatus() {
        checkCommitted();
        return getHttpRespEntity().status;
    }

    @Override
    public String getHeader(String name) {
        checkCommitted();
        return getHttpRespEntity().headers.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        checkCommitted();
        return getHttpRespEntity().headers.getHeaderValues(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        checkCommitted();
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        checkCommitted();
        Charset charset = getHttpRespEntity().charset;
        return charset == null ? null : charset.name();
    }

    @Override
    public String getContentType() {
        checkCommitted();
        return getHttpRespEntity().contentType;
    }

    @Override
    public ByteBufferStream getOutputStream() {
        checkCommitted();
        if (outputStream == null) {
            outputStream = new ByteBufferStream(this);
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        checkCommitted();
        if (printWriter != null) {
            return printWriter;
        }
        CharBufferWriter out = new CharBufferWriter(getOutputStream());
        return printWriter = new PrintWriter(out);
    }

    @Override
    public void setCharacterEncoding(String charset) {
        checkCommitted();
        getHttpRespEntity().charset = Charset.forName(charset.trim());
    }

    @Override
    public void setContentLength(int len) {
        checkCommitted();
        getHttpRespEntity().contentLength = len;
    }

    @Override
    public void setContentLengthLong(long len) {
        checkCommitted();
        getHttpRespEntity().contentLength = len;
    }

    @Override
    public void setContentType(String type) {
        checkCommitted();
        getHttpRespEntity().contentType = type;
    }

    @Override
    public void setBufferSize(int size) {
        checkCommitted();
    }

    @Override
    public int getBufferSize() {
        checkCommitted();
        return 0;
    }

    private void sendHeader() {
        //todo long header
        if (headerSended) return;
        headerSended = true;
        HttpExchanger httpExchanger = getHttpExchanger();
        //todo 规范获取 改成文本协议。
        ByteBuffer writer = httpExchanger.getBuf().borrowByteBuffer();
        writer.clear();
        HttpRespEntity httpRespEntity = getHttpRespEntity();
        //prepare response line
        BufferUtil.put(writer, "HTTP/1.1 ");//必须是大写

        int code = httpRespEntity.status;
        String msg = httpRespEntity.msg;
        if (code == 0) {
            code = httpRespEntity.status = 200;
            msg = "ok";
        }

        writer.put((byte) (code / 100 + '0'));
        writer.put((byte) (code / 10 % 10 + '0'));
        writer.put((byte) (code % 10 + '0'));
        if (msg != null && msg.length() > 0) {
            writer.put((byte) ' ');
            BufferUtil.put(writer, msg);
        }
        BufferUtil.put(writer, " \r\n");


        httpRespEntity.prepareHeader();

        httpRespEntity.headers.getChars(writer);


        if (httpRespEntity.isChunked) {
            responseWriter = new Chunked();
        } else {
            responseWriter = new LengthWriter();
        }


    }

    public void sendBody() {
        sendHeader();
        if (outputStream == null) return;

        ByteBuffer ob = outputStream.getOb();
        ob.flip();
        responseWriter.doWrite(getHttpExchanger(), ob);
    }

    public void end() {
        sendHeader();
        if (outputStream == null) return;

        ByteBuffer ob = outputStream.getOb();
        ob.flip();
        responseWriter.doEnd(getHttpExchanger(), ob);

    }

    @Override
    public void flushBuffer() {
        //do nothing
    }

    @Override
    public void resetBuffer() {
        checkCommitted();
    }

    public void checkCommitted() {
        if (committed) {
            throw new ServerException("response committed");
        }
    }

    @Override
    public boolean isCommitted() {
        return committed;
    }

    @Override
    public void reset() {
        checkCommitted();
    }

    @Override
    public void setLocale(Locale loc) {
        checkCommitted();
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    public void run() {
        checkCommitted();
        if (!headerSended) {
            if (getHttpRespEntity().contentLength == -1L)
                getHttpRespEntity().contentLength = getOutputStream().getOb().position();
        }
        end();
        HttpExchanger httpExchanger = getHttpExchanger();
        httpExchanger.commit();
        //keep alive

        committed = true;
    }


}
