package personal.popy.localserver.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class Async<T> extends FutureTask<T> {

    private Consumer<? super T> consumer;

    public Async(Callable<T> callable) {
        super(callable);
    }

    public synchronized void onSuccess(Consumer<? super T> consumer) {
        if (this.consumer != null) {
            if (isDone()) {
                done(consumer);
            }
            this.consumer = consumer;
        }
    }


    private void done(Consumer<? super T> consumer) {
        try {
            T t = super.get();
            consumer.accept(t);
        } catch (Exception e) {

        }
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        if (consumer != null) {
            throw new IllegalStateException("async context");
        }
        return super.get();
    }

    @Override
    protected synchronized void done() {
        if (consumer != null) {
            done(consumer);
        }
    }
}
