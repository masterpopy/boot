package personal.popy.application.lifecycle;

public interface ServerWorker extends Runnable {
    @Override
    void run();

    void init();
}
