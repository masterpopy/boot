package personal.popy.server.io.stream;

import personal.popy.server.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public class LengthWriter implements ResponseWriter {
    @Override
    public void doWrite(HttpExchanger exchanger, ByteBuffer buffer) {
        exchanger.realWrite(buffer);
    }

    @Override
    public void doEnd(HttpExchanger exchanger, ByteBuffer buffer) {
        exchanger.realWrite(buffer);
    }
}
