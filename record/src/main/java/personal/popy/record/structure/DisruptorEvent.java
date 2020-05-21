package personal.popy.record.structure;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;

public abstract class DisruptorEvent<T> implements EventHandler<T>, EventFactory<T> {


    @Override
    public abstract T newInstance();

    @Override
    public abstract void onEvent(T t, long l, boolean b) throws Exception;

}
