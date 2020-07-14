import org.junit.Test;
import personal.popy.localserver.executor.FastLock;
import sun.security.ssl.SSLEngineImpl;

import javax.net.ssl.SSLContext;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Run {

    @Test
    public void run1() throws Throwable {
        System.setProperty("java.security.debug", "all");
        SSLContext ctx = SSLContext.getDefault();
        SSLEngineImpl sslEngine = (SSLEngineImpl) ctx.createSSLEngine();
        System.out.println(sslEngine.toString());

    }


    @Test
    public void runResponse() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        Ints i = new Ints();

        for (int j = 0; j < 1024; j++) {
            executorService.execute(i);
        }
        try {
            executorService.submit(i).get(50, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {

        }

        System.out.println(i.val);

        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

        List<Thread> threads = i.lock.getThreads();
        for (Thread thread : threads) {

            System.out.println(mxBean.getThreadInfo(thread.getId()));
        }
    }


    private static class Ints implements Runnable {
        private volatile int val;
        private FastLock lock = new FastLock();


        @Override
        public void run() {
            lock.lock();
            val++;
            lock.unlock();
        }
    }

}
