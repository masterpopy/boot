package personal.popy.localserver.io.io;

public interface RequestReader {
    int doRead(byte[] b, int off, int len);
}
