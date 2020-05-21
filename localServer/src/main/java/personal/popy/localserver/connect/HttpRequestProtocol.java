package personal.popy.localserver.connect;

import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.wrapper.HttpReqEntity;

import java.nio.ByteBuffer;

public class HttpRequestProtocol {

    private ChannelStream<HttpReqEntity> stream;

    public HttpRequestProtocol(HttpExchanger exchanger) {
        stream = new ChannelStream<>();
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
        stream.parse(readByteBuffer);
    }


}
