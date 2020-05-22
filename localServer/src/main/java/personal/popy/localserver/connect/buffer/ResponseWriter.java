package personal.popy.localserver.connect.buffer;

import personal.popy.localserver.connect.io.BlockRespWriter;
import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public interface ResponseWriter {

    void doWrite(HttpExchanger exchanger, ByteBuffer buffer);

    void end(HttpExchanger exchanger, ByteBuffer buffer);

    static ResponseWriter newBlock() {
        return new BlockRespWriter();
    }
}
