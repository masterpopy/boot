package personal.popy.server.io.nio;

public interface Key {
    boolean remove();

    Key IMMEDIATE = () -> false;
}
