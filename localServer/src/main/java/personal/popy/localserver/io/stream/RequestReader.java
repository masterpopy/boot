package personal.popy.localserver.io.stream;

public interface RequestReader {
    int doRead(byte[] b, int off, int len);

    int doRead();

    boolean hasRemaining();

    void close();
}
