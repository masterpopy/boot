package personal.popy.localserver.wrapper;

import personal.popy.localserver.data.Headers;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpRespEntity {
    public int status;
    public String contentType;
    public Headers headers = new Headers();
    public Charset charset = StandardCharsets.ISO_8859_1;
    public boolean isChunked;
    public long contentLength = -1;

    public void prepareHeader() {
        if (contentType != null)
        {
            headers.add("content-type", contentType + ";charset=" + charset.name());
        }
        if (contentLength != -1L) {
            headers.add("content-length", String.valueOf(contentLength));
        } else {
            headers.add("Transfer-Encoding", "chunked");
            isChunked = true;
        }

    }

    public void reset() {
        status=0;
        contentLength = -1L;
        contentType = null;
        headers.clear();
        charset = StandardCharsets.ISO_8859_1;
        isChunked = false;
    }
}
