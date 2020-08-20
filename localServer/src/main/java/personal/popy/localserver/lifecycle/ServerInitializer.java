package personal.popy.localserver.lifecycle;

public interface ServerInitializer {
    void onInitialzing(ServerContext context);

    void onInitialized(ServerContext context);
}
