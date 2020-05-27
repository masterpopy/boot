package personal.popy.localserver.executor;

import personal.popy.localserver.util.TimeMonitor;

public class RunEvent {
    public Runnable runnable;


    public void run() {
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
