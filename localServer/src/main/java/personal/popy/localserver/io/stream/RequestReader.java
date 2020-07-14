package personal.popy.localserver.io.stream;

public interface RequestReader {
    int doRead(byte[] b, int off, int len);
}
