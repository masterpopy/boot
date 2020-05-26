package personal.popy.localserver.executor;

import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import personal.popy.localserver.exception.ServerException;
import personal.popy.localserver.servlet.HttpExchanger;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class DisruptorExecutor implements ExecutorService {
    public static final WorkHandler<RunEvent> runEventWorkHandler = RunEvent::run;
    private Disruptor<RunEvent> disruptor;
    private long max;
    private static int BUFFER_SIZE = 1024*32;

    public DisruptorExecutor(int threadCnt) {
        disruptor = newDisruptor(threadCnt);
        disruptor.start();
    }

    private Disruptor<RunEvent> newDisruptor(int threadCnt) {
        Disruptor<RunEvent> disruptor = new Disruptor<>(RunEvent::new, BUFFER_SIZE, Executors.defaultThreadFactory());

        WorkHandler<RunEvent>[] e = new WorkHandler[threadCnt];
        for (int i = 0; i < threadCnt; i++) {
            e[i] = runEventWorkHandler;
        }
        disruptor.handleEventsWithWorkerPool(e);
        return disruptor;
    }

    @Override
    public void shutdown() {
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
        RunEvent runEvent = disruptor.get(next);
        runEvent.start = System.currentTimeMillis();
        runEvent.runnable = command;
        disruptor.getRingBuffer().publish(next);
        max = next;
    }

    public String cal() {
        int time = 0;
        int runTime = 0;
        for (int i = 0; i < BUFFER_SIZE; i++) {
            RunEvent runEvent = disruptor.get(i);
            time += runEvent.totaltime;
            runTime += runEvent.runtime;
        }
        int max = HttpExchanger.suc.get();
        return "总耗时：" + time + ", 已处理：" + max + ", 平均：" + round2(time,max) + ", 总工作用时：" + runTime
        +", 平均工作耗时："+ round2(runTime, max);
    }

    private static float round2(int r, int l) {
        return BigDecimal.valueOf(r).divide(BigDecimal.valueOf(l), 2,BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
