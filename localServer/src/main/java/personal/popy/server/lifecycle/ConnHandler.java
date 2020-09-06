package personal.popy.server.lifecycle;

import org.springframework.context.annotation.Scope;

import java.nio.channels.AsynchronousSocketChannel;

@Scope
public interface ConnHandler {
    void processNewConnection(AsynchronousSocketChannel result);
}
