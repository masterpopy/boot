package personal.popy.localserver.executor;

import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorWorker implements WorkHandler<RunEvent> {
    private Disruptor<RunEvent> dis;

    @Override
    public void onEvent(RunEvent event) throws Exception {

        event.runnable.run();
        event.runnable = null;
    }
}
