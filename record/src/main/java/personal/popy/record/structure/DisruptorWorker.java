package personal.popy.record.structure;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ThreadFactory;

public class DisruptorWorker<T> extends Disruptor<T> {

    public DisruptorWorker(DisruptorEvent<T> event, ThreadFactory factory) {
        super(event, 128, factory);
        handleEventsWith(event);
    }

    @Override
    public RingBuffer<T> start() {
        RingBuffer<T> ringBuffer = super.start();

        for (long l = 0; l < 1024; l++) {

            this.onData(ringBuffer);
        }

        return ringBuffer;
    }


    /**
     * onData用来发布事件，每调用一次就发布一次事件事件
     * 它的参数会通过事件传递给消费者
     *
     * @param ringBuffer ringBuffer
     */
    void onData(RingBuffer<T> ringBuffer) {
        //可以把ringBuffer看做一个事件队列，那么next就是得到下面一个事件槽
        long sequence = ringBuffer.next();
        try {
            //用上面的索引取出一个空的事件用于填充
            ringBuffer.get(sequence);
        } finally {
            //发布事件
            ringBuffer.publish(sequence);
        }
    }

}
