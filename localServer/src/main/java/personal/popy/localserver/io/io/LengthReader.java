package personal.popy.localserver.io.io;

import personal.popy.localserver.servlet.HttpExchanger;

public class LengthReader implements RequestReader{
    private long contentLength;

    private long remaining;

    private HttpExchanger exchanger;



    @Override
    public int doRead(byte[] b, int off, int len) {
        return 0;
    }
}
