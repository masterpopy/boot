package personal.popy.localserver.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorFactory {
    public static ExecutorService newInstance(int threadCnt) {
        return Executors.newFixedThreadPool(threadCnt);
    }

    public static ExecutorService newIo() {
        return Executors.newFixedThreadPool(5);
    }
}
