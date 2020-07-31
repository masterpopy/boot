package personal.popy.localserver.io;

import java.util.List;

public class HttpParseValve implements IoValve {
    private IoValve[] valves;
    private int idx;


    public void ProcessIoEvent(IoEnv env) {

    }

    public HttpParseValve(List<IoValve> valves) {
        this.valves = valves.toArray(new IoValve[0]);
    }

}
