import org.junit.Test;
import personal.popy.server.executor.FastLock;

import javax.xml.bind.JAXBContext;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Run {

    private class GbaCharset extends CharsetProvider {


        @Override
        public Iterator<Charset> charsets() {
            return null;
        }

        @Override
        public Charset charsetForName(String charsetName) {
            return null;
        }
    }
    @Test
    public void run1() throws Throwable {

        JAXBContext jaxbContext=JAXBContext.newInstance(Root.class);
        Root unmarshal = (Root) jaxbContext.createUnmarshaller().unmarshal(getClass().getResource("root.xml"));
        for (ValidOption validOption : unmarshal.validOptions) {
            System.out.print("[--"+validOption.name+"="+validOption.name+"] ");
        }


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
