package personal.popy.localserver.connect.io;

import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public class LengthWriter implements ResponseWriter {
    @Override
    public void doWrite(HttpExchanger exchanger, ByteBuffer buffer) {
        exchanger.realWrite(buffer);
    }

    @Override
    public void end(HttpExchanger exchanger, ByteBuffer buffer) {
        exchanger.realWrite(buffer);
    }
}
