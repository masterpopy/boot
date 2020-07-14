package personal.popy.localserver.lifecycle;

import personal.popy.localserver.servlet.HttpExchanger;

public interface RequestWorker {
    void doWork(HttpExchanger exchanger);
}
