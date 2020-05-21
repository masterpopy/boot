package personal.popy.localserver.executor;

import java.util.concurrent.ExecutorService;

public class ExecutorFactory {
    public static ExecutorService newInstance(int threadCnt) {
        return new DisruptorExecutor(threadCnt);
    }
}
