package personal.popy.server.wrapper;

import personal.popy.server.servlet.data.Headers;

import java.nio.charset.Charset;

public class HttpRespEntity {
    public int status;
    public String msg;
    public String contentType;
    public Headers headers = new Headers();
    public Charset charset = null;//除非用户自己设置charset，否则不传charset
    public boolean isChunked;
    public long contentLength = -1;

    public void prepareHeader() {
        if (contentType != null)
        {
            headers.put("content-type",charset == null ? contentType : contentType + ";charset=" + charset.name());
        }
        if (contentLength != -1L) {
            headers.put("content-length", String.valueOf(contentLength));
        } else {
            headers.put("Transfer-Encoding", "chunked");
            isChunked = true;
        }

    }

    public void reset() {
        status=0;
        msg="";
        contentLength = -1L;
        contentType = null;
        headers.clear();
        charset = null;
        isChunked = false;
    }
}
