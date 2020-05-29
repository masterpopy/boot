package personal.popy.localserver.servlet;

import personal.popy.localserver.connect.aio.ChannelStream;
import personal.popy.localserver.wrapper.HttpReqEntity;

import java.nio.ByteBuffer;

public class HttpRequestProtocol {

    private ChannelStream stream;

    public HttpRequestProtocol(HttpExchanger exchanger) {
        stream = new ChannelStream();
        stream.setData(new HttpReqEntity());
        stream.readToSpace((e, s) -> e.method = s.getLong())
                .readToSpace((e, s) -> s.decodeUrl(e))
                .readToLine((e, s) -> e.protocol = s.ansiString())
                .readToLine(HttpReqEntity::parseHeader)
                .loop();
        stream.setHandler(exchanger);
    }

    public void asyncParse(ByteBuffer readByteBuffer) {
        stream.reset();
        stream.require(readByteBuffer);
    }

}
