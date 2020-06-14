import org.junit.Test;
import personal.popy.localserver.executor.FastLock;
import sun.security.ssl.SSLEngineImpl;

import javax.net.ssl.SSLContext;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ExecutorService e = Executors.newFixedThreadPool(30, threadFactory);
        IntRun intRun = new IntRun();
        for (int i = 0; i < 1024; i++) {
            e.execute(intRun);
        }
        Thread.sleep(50);

        /*Field group = threadFactory.getClass().getDeclaredField("group");
        group.setAccessible(true);
        Thread[] s = new Thread[30];
        int enumerate = ((ThreadGroup) group.get(threadFactory)).enumerate(s,false);
        for (int i = 0; i < enumerate; i++) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(s[i].getId());
            System.out.print(threadInfo.toString());
        }*/
        System.out.println(intRun.i);


    }

    private static class IntRun implements Runnable {


        private FastLock f = new FastLock();
        private int i;

        @Override
        public void run() {
            f.lock();
            i++;
            f.unlock();
        }
    }

    private class type {
        private String[] attr;
    }

}
