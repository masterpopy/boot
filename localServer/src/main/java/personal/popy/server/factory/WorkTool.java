package personal.popy.server.factory;

import personal.popy.server.exception.UnHandledException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WorkTool {
    private Object target;
    private Method before;
    private Method run;

    private WorkTool next;

    public void before() {
        if (target != null && before != null) {
            try {
                before.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new UnHandledException(e);
            }
        }
    }

    public void run() {
        if (target != null && run != null) {
            try {
                run.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new UnHandledException(e);
            }
        }

        next.run();
    }
}
