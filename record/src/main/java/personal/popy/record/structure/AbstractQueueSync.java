package personal.popy.record.structure;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

@SuppressWarnings("unchecked")
public class AbstractQueueSync<T> extends AbstractQueuedSynchronizer {
    Node[] elements;
    int head;
    int tail;
    int lengthMask;

    public AbstractQueueSync() {
        lengthMask = 3;
        elements = new Node[lengthMask + 1];
    }

    public void add(T t) {


    }

    Node next() {


        return null;
    }


    public T get() {
        return null;
    }

    private static class Node {
        Object th;

        @Override
        public String toString() {
            return th.toString();
        }
    }

}
