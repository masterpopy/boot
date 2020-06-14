package personal.popy.localserver.action;

public interface ReadAction {

     CompletedStatus onData(Integer result, java.nio.ByteBuffer buffer);

}
