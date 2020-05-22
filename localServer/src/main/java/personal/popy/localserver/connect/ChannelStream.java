package personal.popy.localserver.connect;

import personal.popy.localserver.action.CharCondition;
import personal.popy.localserver.action.CompletedStatus;
import personal.popy.localserver.action.ReadAction;
import personal.popy.localserver.wrapper.SliencedBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ChannelStream<T> implements CompletionHandler<Integer, ByteBuffer>, StreamHandler<T> {
    private List<ReadAction> stream = new ArrayList<>(5);

    //传输处理成功后的buff
    private SliencedBuffer origin;
    //stream 的位置
    private int processIndex;
    //buff读取到的位置
    private int readIndex;
    //当前stream处理时buff的起始位置
    private int curStart;
    private T data;

    private StreamHandler<T> handler;


    public void setHandler(StreamHandler<T> handler) {
        this.handler = handler;
    }

    private static final CharCondition meetLf = ch -> ch == '\r';
    private static final CharCondition meetCR = ch -> ch == '\n';
    private static final CharCondition meetSp = ch -> ch == ' ' || ch == '\t';

    private static final CharCondition[] READ_UNTIL_END_LINE = {meetLf, meetCR};

    public ChannelStream() {
        origin = new SliencedBuffer();
    }

    public void setData(T data) {
        this.data = data;
    }

    public SliencedBuffer getOrigin() {
        return origin;
    }


    public ChannelStream<T> readToSpace(BiConsumer<T, SliencedBuffer> w) {
        stream.add(((result, buffer) -> {
            int position = buffer.position();
            while (readIndex < position) {
                byte b = buffer.get(readIndex++);
                if (meetSp.meet(b)) {
                    w.accept(data, origin.reset(buffer, curStart, readIndex - 1));
                    return CompletedStatus.SUCCESS;
                }
            }
            return CompletedStatus.REQUIRE_MORE_DATA;
        }));
        return this;
    }

    public ChannelStream<T> readToLine(BiConsumer<T, SliencedBuffer> w) {
        stream.add(((result, buffer) -> {
            int position = buffer.position();
            OUT:
            while (readIndex + READ_UNTIL_END_LINE.length <= position) {//required bytes
                for (int i = 0; i < READ_UNTIL_END_LINE.length; i++) {
                    char b = (char) buffer.get(readIndex + i);
                    if (!READ_UNTIL_END_LINE[i].meet(b)) {
                        //匹配失败，当前字节数提升1
                        readIndex += 1;
                        continue OUT;
                    }
                }
                //匹配成功
                readIndex += READ_UNTIL_END_LINE.length;
                SliencedBuffer reset = origin.reset(buffer, curStart, readIndex - READ_UNTIL_END_LINE.length);
                if (reset.getLength() == READ_UNTIL_END_LINE.length) {
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


    public ChannelStream<T> loop() {
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
            readAction.completed(result, buffer);
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
            CompletedStatus exe = cur.completed(buffer.position() - curStart, buffer);
            switch (exe) {
                case REQUIRE_MORE_DATA:
                    require(buffer, this);
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

    @Override
    public void success(T t) {
        handler.success(t);
    }


    @Override
    public void require(ByteBuffer b, CompletionHandler<Integer, ByteBuffer> c) {
        handler.require(b, c);
    }

    @Override
    public void error() {
        handler.error();
    }

    @Override
    public void closed() {
        handler.closed();
    }
}
