package personal.popy.localserver.io.io;

import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public interface ResponseWriter {

    void doWrite(HttpExchanger exchanger, ByteBuffer buffer);

    void doEnd(HttpExchanger exchanger, ByteBuffer buffer);

    static ResponseWriter newBlock() {
        return new BlockRespWriter();
    }
}
