package personal.popy.localserver.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class ProcessBuffer {
    private final ByteBuffer streamBuf;//4kb
    private final ByteBuffer writerBuf;//8kb

    private final CharBuffer charBuf;

    private Future realWriteTask;
    public ProcessBuffer() {
        streamBuf = ByteBuffer.allocate(1024 * 4);
        writerBuf = ByteBuffer.allocateDirect(1024 * 8);
        charBuf = CharBuffer.allocate(1024);
    }


    public ByteBuffer getStreamBuf() {
        return streamBuf;
    }

    public ByteBuffer getWriterBuf() {
        if (realWriteTask != null) {
            try {
                realWriteTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            realWriteTask = null;
            writerBuf.clear();
        }
        return writerBuf;
    }

    public CharBuffer getCharBuf() {
        return charBuf;
    }


    public void clear() {
        writerBuf.clear();
        streamBuf.clear();
        charBuf.clear();
    }

    public void setRealWriteTask(Future realWriteTask) {
        this.realWriteTask = realWriteTask;
    }
}
