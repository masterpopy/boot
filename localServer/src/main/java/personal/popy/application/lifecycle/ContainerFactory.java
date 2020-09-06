package personal.popy.application.lifecycle;

public interface ContainerFactory {
    <T extends Container> T create(Class<T> clz);
}
