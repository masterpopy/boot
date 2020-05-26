package personal.popy.localserver.executor;

public class RunEvent {
    public Runnable runnable;
    public int totaltime=0;
    public long start=0;

    public int runtime=0;

    public void run() {
        long l = System.currentTimeMillis();
        runnable.run();
        runnable = null;
        long l1 = System.currentTimeMillis();
        this.totaltime += (l1 - start);
        this.runtime += (l1 - l);
    }
}
