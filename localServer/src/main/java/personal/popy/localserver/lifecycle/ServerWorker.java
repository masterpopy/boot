package personal.popy.localserver.lifecycle;

public interface ServerWorker extends Runnable {
    @Override
    void run();

    void init();

    void destory();
}
