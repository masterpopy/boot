import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import personal.popy.localserver.executor.FastLock;

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

        Observable.fromPublisher(new Publisher<String>() {
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long l) {

                    }

                    @Override
                    public void cancel() {

                    }
                });
                subscriber.onNext("321");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });


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
