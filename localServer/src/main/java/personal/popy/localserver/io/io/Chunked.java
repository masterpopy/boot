package personal.popy.localserver.io.io;

import personal.popy.localserver.servlet.HttpExchanger;

import java.nio.ByteBuffer;

public class Chunked implements ResponseWriter{
    /*private static final byte[] LAST_CHUNK_BYTES = {(byte) '0', (byte) '\r', (byte) '\n'};
    private static final byte[] CRLF_BYTES = {(byte) '\r', (byte) '\n'};*/
    private static final byte[] END_CHUNK_BYTES =
            {(byte) '0', (byte) '\r', (byte) '\n', (byte) '\r', (byte) '\n'};

    /**
     * Table for DEC to HEX byte translation.
     */
    private static final byte[] HEX =
            {(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
                    (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
                    (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'};
    /**
     * Chunk header.
     */
    protected final ByteBuffer chunkHeader = ByteBuffer.allocate(10);


    /*protected final ByteBuffer lastChunk = ByteBuffer.wrap(LAST_CHUNK_BYTES);
    protected final ByteBuffer crlfChunk = ByteBuffer.wrap(CRLF_BYTES)*/;
    /**
     * End chunk.
     */
    protected final ByteBuffer endChunk = ByteBuffer.wrap(END_CHUNK_BYTES);


    public Chunked() {
        chunkHeader.put(8, (byte) '\r');
        chunkHeader.put(9, (byte) '\n');
    }

    private int calculateChunkHeader(int len) {
        // Calculate chunk header
        int pos = 8;
        int current = len;
        while (current > 0) {
            int digit = current % 16;
            current = current / 16;
            chunkHeader.put(--pos, HEX[digit]);
        }
        return pos;
    }

    @Override
    public void doWrite(HttpExchanger exchanger, ByteBuffer chunk) {
        int result = chunk.remaining();
        if (result == 0) return;
        int pos = calculateChunkHeader(result);
        chunkHeader.position(pos).limit(10);
        exchanger.realWrite(chunkHeader);
        exchanger.realWrite(chunk);
        chunkHeader.position(8).limit(10);
        exchanger.realWrite(chunkHeader);
    }

    @Override
    public void doEnd(HttpExchanger exchanger, ByteBuffer chunk) {
        // Write end chunk
        doWrite(exchanger, chunk);
        exchanger.realWrite(endChunk);
        endChunk.clear();
    }

}
