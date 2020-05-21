package personal.popy.localserver.executor;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public abstract class CompleteRunnable implements Runnable, CompletionHandler<Long, ByteBuffer> {
}
