package personal.popy.localserver.io;


import java.nio.ByteBuffer;

interface AsyncListener {
    boolean signal();

    ByteBuffer getBuffer();
}
