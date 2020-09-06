package personal.popy.server.action;

public interface ReadAction {

     CompletedStatus onData(Integer result, java.nio.ByteBuffer buffer);

}
