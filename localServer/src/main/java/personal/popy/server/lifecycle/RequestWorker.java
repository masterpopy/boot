package personal.popy.server.lifecycle;

import personal.popy.server.servlet.HttpExchanger;

public interface RequestWorker {
    void doWork(HttpExchanger exchanger);
}
