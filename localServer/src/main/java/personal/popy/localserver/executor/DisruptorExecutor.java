package personal.popy.localserver.executor;

import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import personal.popy.localserver.data.StaticBuffer;
import personal.popy.localserver.exception.ServerException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class DisruptorExecutor implements ExecutorService {
    public static final WorkHandler<RunEvent> runEventWorkHandler = event -> {
        event.runnable.run();
        event.runnable = null;

    };
    private Disruptor<RunEvent> disruptor;
    private long max;

    public DisruptorExecutor(int threadCnt) {
        disruptor = newDisruptor(threadCnt);
        disruptor.start();
    }

    private Disruptor<RunEvent> newDisruptor(int threadCnt) {
        Disruptor<RunEvent> disruptor = new Disruptor<>(RunEvent::new, 1024 * 8, Executors.defaultThreadFactory());

        WorkHandler<RunEvent>[] e = new WorkHandler[threadCnt];
        for (int i = 0; i < threadCnt; i++) {
            e[i] = runEventWorkHandler;
        }
        disruptor.handleEventsWithWorkerPool(e);
        return disruptor;
    }

    @Override
    public void shutdown() {
        System.out.println("已处理：" + max);
        System.out.println("1池大小：" + StaticBuffer.buffered.bufferBytes.size());
        System.out.println("8池大小：" + StaticBuffer.buffered.bufferBytes8.size());
        disruptor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> s = new FutureTask<>(task);
        execute(s);
        return s;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return submit(Executors.callable(task, result));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return submit(task, null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new ServerException("不支持的操作");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new ServerException("不支持的操作");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new ServerException("不支持的操作");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new ServerException("不支持的操作");
    }

    @Override
    public void execute(Runnable command) {
        long next = disruptor.getRingBuffer().next();
        disruptor.get(next).runnable = command;
        disruptor.getRingBuffer().publish(next);
        max = next;
    }
}
