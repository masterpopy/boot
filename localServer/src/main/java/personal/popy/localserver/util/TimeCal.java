package personal.popy.localserver.util;

public class TimeCal {
    private long nanoTime;

    public void add(long nanoTime) {
        this.nanoTime += nanoTime;
    }

    public double get(int r) {
        double v = ((double) nanoTime) / r;
        return  v / 1000000;
    }
}
