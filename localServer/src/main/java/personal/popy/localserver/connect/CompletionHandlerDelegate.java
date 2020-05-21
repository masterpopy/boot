package personal.popy.localserver.connect;

import java.nio.channels.CompletionHandler;
import java.util.function.BiConsumer;

public class CompletionHandlerDelegate<V, A> implements CompletionHandler<V, A> {
    BiConsumer<V, A> completed;

    public void setCompleted(BiConsumer<V, A> completed) {
        this.completed = completed;
    }

    public void setFailed(BiConsumer<Throwable, A> failed) {
        this.failed = failed;
    }

    BiConsumer<Throwable, A> failed;

    public CompletionHandlerDelegate(BiConsumer<V, A> completed, BiConsumer<Throwable, A> failed) {
        this.completed = completed;
        this.failed = failed;
    }

    public CompletionHandlerDelegate(){}

    @Override
    public void completed(V result, A attachment) {
        if (completed != null) {
            completed.accept(result, attachment);
        }
    }

    @Override
    public void failed(Throwable exc, A attachment) {
        if (failed != null) {
            failed.accept(exc, attachment);
        }
    }
}
