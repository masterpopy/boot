package personal.popy.server.wrapper;

import personal.popy.server.servlet.data.Headers;
import personal.popy.server.servlet.data.Parameters;

import java.nio.ByteBuffer;


public class HttpReqEntity {
    public long method;
    public String uri;// ${contextPath}/...uri


    public String query;
    public String protocol;
    public Headers headers = new Headers();
    public Parameters parameters = new Parameters();

    public String encoding;

    public long contentLength = -1L;

    public static final int     GET     = 0x474554;
    public static final int     POST    = 0x504f5354;
    public static final long    DELETE  = 0x44454c455445L;
    public static final int     PUT     = 0x505554;
    public static final int     HEAD    = 0x48454144;
    public static final long    TRACE   = 0x5452414345L;
    public static final long    OPTIONS = 0x4f5054494f4e53L;
    public static final long    CONNECT = 0x434f4e4e454354L;

    public String getMethod() {
        if (method == GET) {
            return "GET";
        } else if (method == POST) {
            return "POST";
        } else if (method == DELETE) {
            return "DELETE";
        } else if (method == PUT) {
            return "PUT";
        } else if (method == HEAD) {
            return "HEAD";
        } else if (method == TRACE) {
            return "TRACE";
        } else if (method == OPTIONS) {
            return "OPTIONS";
        } else if (method == CONNECT) {
            return "CONNECT";
        }
        return "UNKNOWN";
    }

    //todo range check
    public void parseHeader(SliencedBuffer bytes) {
        int start = bytes.getStart();
        int pos = bytes.getLimit();
        ByteBuffer origin = bytes.getOrigin();

        //1. if header start with space that means this is a multiline header
        if (origin.get(start) == ' ') {
            //todo
        }
        String key = null;
        while (start < pos) {
            if (origin.get(start++) == ':') {
                //head value found;
                int start1 = bytes.getStart();
                key = bytes.stringValue(start1, start - start1 - 1);
                break;
            }

        }

        while (true) {
            byte b = origin.get(start);
            if (!(b == ' ' || b == '\t')) break;
            start++;
        }
        String value = bytes.stringValue(start, pos - start);
        //todo content-length check
        headers.put(key, value);
    }

    @Override
    public String toString() {
        return "HttpReqEntity{" +
                "method='" + getMethod() + '\'' +
                ", url='" + uri + '\'' +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers.toString() +
                '}';
    }

    public String getContentType() {
        return "";
    }

    public void recycle() {
        uri = null;
        method = 0L;
        query = null;
        protocol = null;
        headers.clear();
        parameters.clear();
        encoding = "UTF-8";
        contentLength = -1L;
    }

    public long getContentLength() {
        if (contentLength == -1L) {
            String s = headers.get("content-length");
            contentLength = Long.parseLong(s);
        }
        return contentLength;
    }
}
