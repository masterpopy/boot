import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.CompletionHandler;

public class Master {
    private IoStream[] players;

    private boolean isAllPlayerConnected;

    private ByteBuffer buffer ;

    private final int max;

    private String username;

    private final int[] data = {0, 0xffff434d, 0xffff834d, 0xffffc34d};


    public Master(int max, String username, IoStream io) {
        players = new IoStream[max];
        players[0] = io;
        this.username = username;
        this.max = max;
        buffer= ByteBuffer.allocate(32).order(ByteOrder.LITTLE_ENDIAN);
    }

    public boolean add(IoStream io) {
        if (isAllPlayerConnected) throw new IllegalStateException("refuse");
        int i = 1;
        for (; i < max; i++) {
            if (players[i] == null) {
                players[i] = io;
                if (i == max - 1) {
                    isAllPlayerConnected = true;
                    InitLink();
                }
                break;
            }
        }
        return isAllPlayerConnected;
    }


    protected void InitLink() {
        //first, send id to all players;

        for (int i = 0; i < max; i++) {
            buffer.putInt(i);
            buffer.flip();
            players[i].write(buffer);
            buffer.clear();
        }

        //recieve 8 from master; async
        BeginRead();

    }

    protected void processTransfer() {
        if (buffer.get(0) == 'S') {
            //todo close
            buffer.flip();
            players[1].write(buffer);
            players[0].close();
            players[1].close();

            return;
        }

        buffer.flip();
//        System.out.println("read from player0:" + Long.toHexString(buffer.getLong(0)));

        players[1].write(buffer);

        buffer.clear().limit(4);

        players[1].read(buffer);
//        System.out.println("read from player1:" + Integer.toHexString(buffer.getInt(0)));

        buffer.flip();

        players[0].write(buffer);

        buffer.clear();
        BeginRead();
    }

    protected void BeginRead() {
        buffer.limit(8);
        players[0].read(buffer, this, new CompletionHandler<Integer, Master>() {
            @Override
            public void completed(Integer result, Master attachment) {
                attachment.processTransfer();
            }

            @Override
            public void failed(Throwable exc, Master attachment) {
                exc.printStackTrace();
            }
        });
    }
}
