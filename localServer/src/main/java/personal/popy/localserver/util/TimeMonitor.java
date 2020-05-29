package personal.popy.localserver.util;

public class TimeMonitor {
    private long time;
    private long start;
    private boolean started;

    public void timeStart() {
        this.start = System.nanoTime();
        started = true;
    }

    public void timeEnd() {
        if (!started) {

            return;
        }
        time += System.nanoTime() - start;
        started = false;
    }

    public long getTime() {
        return time;
    }
}
