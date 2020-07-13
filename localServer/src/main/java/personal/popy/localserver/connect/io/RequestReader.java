package personal.popy.localserver.connect.io;

public interface RequestReader {
    int doRead(byte[] b, int off, int len);
}
