package personal.popy.server.executor;

import personal.popy.server.util.TimeMonitor;

public class RunEvent {
    public Runnable runnable;


    public void run() {
        if (runnable == null) return;
        if (runnable instanceof TimeMonitor) {
            TimeMonitor stage = ((TimeMonitor) runnable);
            stage.timeStart();
            runnable.run();
            runnable = null;
        } else {
            runnable.run();
            runnable = null;
        }

    }
}
