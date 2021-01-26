import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class AcceptorHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

    private final HashMap<String, Master> players = new HashMap<>();
    private final ByteBuffer buffer = ByteBuffer.allocate(32);

    @Override
    public void completed(AsynchronousSocketChannel result, Void _void) {
        try {
            Integer integer = result.read(buffer).get();

            String playerName = new String(buffer.array(), 0, integer);
            System.out.println(playerName);
            if (players.containsKey(playerName)) {
                Master master = players.get(playerName);
                if (master.add(new AioStream(result))) {
                    players.remove(playerName);
                }
            } else {
                players.put(playerName, new Master(2, playerName, new AioStream(result)));
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, Void _void) {

    }
}
