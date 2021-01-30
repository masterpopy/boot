import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class AcceptorHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

    private final ConcurrentHashMap<String, Master> players = new ConcurrentHashMap<>();

    private final ByteBuffer buffer = ByteBuffer.allocate(32);

    @Override
    public void completed(AsynchronousSocketChannel result, Void _void) {
        try {
            result.read(buffer).get();
            buffer.flip();
            int anInt = buffer.getInt();
            buffer.clear();
            String playerName = Integer.toString(anInt);
            System.out.println("playerId:" + anInt + " connected");
            Consumer<Throwable> s = t -> {
                System.out.println(playerName + " disconnected");
                players.remove(playerName);
            };
            if (players.containsKey(playerName)) {
                Master master = players.get(playerName);
                if (master.add(new AioStream(result, s))) {
                    players.remove(playerName);
                }
            } else {
                players.put(playerName, new Master(2, new AioStream(result, s)));
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, Void _void) {
        exc.printStackTrace();
    }
}
