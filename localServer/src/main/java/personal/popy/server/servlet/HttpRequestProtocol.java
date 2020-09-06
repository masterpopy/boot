package personal.popy.server.servlet;

import personal.popy.server.io.aio.ChannelStream;
import personal.popy.server.wrapper.HttpReqEntity;

import java.nio.ByteBuffer;

//这个类负责解析请求。
public class HttpRequestProtocol {

    private ChannelStream stream;

    public HttpRequestProtocol(HttpExchanger exchanger) {
        stream = new ChannelStream();
        stream.setData(new HttpReqEntity());
        stream.readToSpace((e, s) -> e.method = s.getLong())
                .readToSpace((e, s) -> s.decodeUrl(e))
                .readToLine((e, s) -> e.protocol = s.stringValue())
                .readToLine(HttpReqEntity::parseHeader)
                .loop();
        stream.setHandler(exchanger);
    }

    public void asyncParse(ByteBuffer readByteBuffer) {
        stream.reset();
        stream.require(readByteBuffer);
    }

}
