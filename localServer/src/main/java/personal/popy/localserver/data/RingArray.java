package personal.popy.localserver.data;

import java.util.concurrent.atomic.AtomicInteger;

public class RingArray {

    private Object[] array;


    private final AtomicInteger end = new AtomicInteger(-1);


    private final AtomicInteger start = new AtomicInteger(-1);

    private static final StateMachineFactory machine;

    private static final StateMachineFactory.StateInfo normal;

    private static final StateMachineFactory.StateInfo getWaiting;

    private static final StateMachineFactory.StateInfo setWaiting;

    private final StateMachineFactory.StateMachine stateMachine = machine.newInstance();


    static {
        machine = new StateMachineFactory();
        normal = machine.reg(1, 2);
        getWaiting = machine.reg(3, 4);
        setWaiting = machine.reg(5, 6);
        machine.init(1);
    }

    public Object getNext() {
        int i = start.incrementAndGet();

        if (i <= end.get()) {
            return array[i & (array.length - 1)];
        } else {
            synchronized (start) {
                try {
                    start.wait();
                } catch (InterruptedException ignored) {

                }
            }
        }
        return getNext();
    }

    public void putNext(Object c) {
        int mask = array.length - 1;
        int i = end.incrementAndGet();
        int j = start.get();
        if (i - j <= array.length) {
            array[i & mask] = c;
        } else {
            synchronized (end) {
                try {
                    end.wait();
                } catch (InterruptedException ignored) {

                }
            }
            putNext(c);
        }
    }

}
