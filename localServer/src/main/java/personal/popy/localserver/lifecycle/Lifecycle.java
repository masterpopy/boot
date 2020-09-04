package personal.popy.localserver.lifecycle;

public interface Lifecycle {
    void init(ServerContext context);

    void start(ServerContext context);
}
