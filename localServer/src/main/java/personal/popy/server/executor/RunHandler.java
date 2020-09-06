package personal.popy.server.executor;

import com.lmax.disruptor.EventHandler;

public class RunHandler implements EventHandler<RunEvent> {
    @Override
    public void onEvent(RunEvent event, long sequence, boolean endOfBatch) throws Exception {
        event.runnable.run();
        event.runnable = null;
    }
}
