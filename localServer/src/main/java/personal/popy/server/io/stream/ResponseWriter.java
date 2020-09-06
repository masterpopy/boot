package personal.popy.server.io.stream;

import personal.popy.server.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public interface ResponseWriter {

    void doWrite(HttpExchanger exchanger, ByteBuffer buffer);

    void doEnd(HttpExchanger exchanger, ByteBuffer buffer);

    static ResponseWriter newBlock() {
        return new BlockRespWriter();
    }
}
