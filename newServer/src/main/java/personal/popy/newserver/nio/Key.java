package personal.popy.newserver.nio;

public interface Key {
    boolean remove();

    Key IMMEDIATE = () -> false;
}
