package personal.popy.server.io.aio;

import personal.popy.server.action.CompletedStatus;
import personal.popy.server.action.ReadAction;
import personal.popy.server.lifecycle.HttpWorker;
import personal.popy.server.servlet.HttpExchanger;
import personal.popy.server.util.BufferUtil;
import personal.popy.server.wrapper.HttpReqEntity;
import personal.popy.server.wrapper.SliencedBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ChannelStream implements CompletionHandler<Integer, ByteBuffer>, HttpWorker {
    private List<ReadAction> stream = new ArrayList<>(5);

    //传输处理成功后的buff
    private SliencedBuffer origin;
    //stream 的位置
    private int processIndex;
    //buff读取到的位置
    private int readIndex;
    //当前stream处理时buff的起始位置
    private int curStart;
    private HttpReqEntity data;

    private HttpExchanger handler;


    public void setHandler(HttpExchanger handler) {
        this.handler = handler;
    }

    /*private static final CharCondition meetLf = ch -> ch == '\r';
    private static final CharCondition meetCR = ch -> ch == '\n';
    private static final CharCondition meetSp = ch -> ch == ' ' || ch == '\t';*/



    public ChannelStream() {
        origin = new SliencedBuffer();
    }

    public void setData(HttpReqEntity data) {
        this.data = data;
    }

    public ChannelStream readToSpace(BiConsumer<HttpReqEntity, SliencedBuffer> w) {
        stream.add(((result, buffer) -> {
            int position = buffer.position();
            while (readIndex < position) {
                byte b = buffer.get(readIndex++);
                if (b == ' ' || b == '\t') {
                    w.accept(data, origin.reset(buffer, curStart, readIndex - 1));
                    return CompletedStatus.SUCCESS;
                }
            }
            return CompletedStatus.REQUIRE_MORE_DATA;
        }));
        return this;
    }

    public ChannelStream readToLine(BiConsumer<HttpReqEntity, SliencedBuffer> w) {
        stream.add(((result, buffer) -> {
            int position = buffer.position();
            byte[] lineSep = BufferUtil.LINE_SEP;
            OUT:
            while (readIndex + lineSep.length <= position) {//required bytes
                for (int i = 0; i < lineSep.length; i++) {
                    char b = (char) buffer.get(readIndex + i);
                    if (lineSep[i] != b) {
                        //匹配失败，当前字节数提升1
                        readIndex += 1;
                        continue OUT;
                    }
                }
                //匹配成功
                readIndex += lineSep.length;
                SliencedBuffer reset = origin.reset(buffer, curStart, readIndex - lineSep.length);
                if (reset.getLength() == lineSep.length) {
                    //empty give up
                    readIndex -= 2;
                } else {
                    w.accept(data, reset);
                }
                return CompletedStatus.SUCCESS;

            }
            return CompletedStatus.REQUIRE_MORE_DATA;
        }));
        return this;
    }


    public ChannelStream loop() {
        stream.add((result, buffer) -> {
            if (result < 2) { //require
                return CompletedStatus.REQUIRE_MORE_DATA;
            }
            if (buffer.get(readIndex) == '\r' && buffer.get(readIndex + 1) == '\n') {
                readIndex += 2;
                return CompletedStatus.SUCCESS;
            }
            ReadAction readAction = stream.get(processIndex - 1);
            //readAction.reset(getStart());
            readAction.onData(result, buffer);
            return CompletedStatus.REPEAT;
        });
        return this;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (result == -1) {
            //stream has closed;
            closed();
            return;
        }
        parse(attachment);
    }


    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        //timeout happend ,this stream has not finish
        exc.printStackTrace();
        error();
    }


    public void parse(ByteBuffer buffer) {
        for (; processIndex < stream.size(); processIndex++) {
            ReadAction cur = stream.get(processIndex);
            CompletedStatus exe = cur.onData(buffer.position() - curStart, buffer);
            switch (exe) {
                case REQUIRE_MORE_DATA:
                    buffer.position(curStart);//丢弃前面的字节。
                    buffer.compact();
                    readIndex = readIndex - curStart;
                    curStart = 0;
                    require(buffer);
                    return;
                case SUCCESS:
                    curStart = readIndex;// must be here
                    continue;
                case REPEAT:
                    curStart = readIndex;// must be here
                    processIndex--;
                    continue;
                case ERRO:
                    error();
                    return;
            }
            //continue with current thread
        }
        buffer.flip();
        buffer.position(readIndex);
        success(data);
    }


    public void reset() {
        readIndex = 0;
        curStart = 0;
        processIndex = 0;
    }

    public void success(HttpReqEntity t) {
        handler.success(t);
    }


    public void require(ByteBuffer b) {
        handler.require(b, this);
    }

    public void error() {
        handler.error();
    }

    public void closed() {
        handler.closed();
    }

    @Override
    public void run() {
        //TODO 如果readbuf有剩余，则交给IO线程去做
        //不能这样用，write阻塞太久不行
        parse(handler.getReadBuf());
    }
}
