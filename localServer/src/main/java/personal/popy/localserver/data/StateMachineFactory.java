package personal.popy.localserver.data;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StateMachineFactory {


    public static class StateInfo {
        private final int s1;
        private final int s2;

        private StateInfo(int s1, int s2) {
            this.s1 = s1;
            this.s2 = s2;
        }

        private StateInfo compareAndSet(StateMachine machine) {
            AtomicInteger state = machine.state;
            int a = state.get();
            if (a == s1) {
                if (state.compareAndSet(s1, s2)) {
                    state.set(s1);
                    return this;
                }
                //设置失败，有两种可能：同样的状态设置成功，那么等待它将值设置回去。
                int i = state.get();
                if (i == s2) {
                    while (state.get() == s2) {
                        Thread.yield();
                    }
                    return compareAndSet(machine);
                }
                if (state.get() == s1) {
                    return compareAndSet(machine);
                }
                a = i;
            }
            //已经确定被另一个不同的状态设置，返回该状态
            return machine.reged.get(a);
        }
    }


    private int state;
    private boolean inited;

    private ArrayList<StateInfo> reged = new ArrayList<>(3);


    public StateInfo reg(int s1, int s2) {
        if (inited) {
            throw new IllegalStateException("初始化已经完成，后续无法修改");
        }
        StateInfo e = new StateInfo(s1, s2);
        reged.add(e);
        return e;
    }

    public void init(int first) {
        state = first;
        inited = true;
    }

    public StateMachine newInstance() {
        return new StateMachine(new AtomicInteger(state), reged);
    }


    public static class StateMachine {
        public StateMachine(AtomicInteger state, ArrayList<StateInfo> reged) {
            this.state = state;
            this.reged = reged;
        }

        private AtomicInteger state;
        private ArrayList<StateInfo> reged;

        public StateInfo checkState(StateInfo info) {

            return info.compareAndSet(this);
        }

    }
}
