package personal.popy.localserver.util;

import personal.popy.localserver.servlet.HttpExchanger;

public class TimeCal {
    private long nanoTime;

    public void add(long nanoTime) {
        this.nanoTime += nanoTime;
    }

    public double get() {
        double v = ((double) nanoTime) / HttpExchanger.suc.get();
        return  v / 1000000;
    }
}
