package personal.popy.localserver.connect.io;

import personal.popy.localserver.connect.buffer.ResponseWriter;
import personal.popy.localserver.servlet.HttpExchanger;

import javax.servlet.WriteListener;
import java.nio.ByteBuffer;

public class NonBlockRespWriter implements ResponseWriter {
    private WriteListener writeListener;
    @Override
    public void doWrite(HttpExchanger exchanger, ByteBuffer buffer) {

    }

    @Override
    public void end(HttpExchanger exchanger, ByteBuffer buffer) {

    }
}
