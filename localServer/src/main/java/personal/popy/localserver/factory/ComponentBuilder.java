package personal.popy.localserver.factory;

public class ComponentBuilder {
    private FactoryInitializer initializer;

    private WebResource webResource = new ClassLoaderResource();

    private ComponentFactory factory;

    public void setInitializer(FactoryInitializer initializer) {
        this.initializer = initializer;
    }

    public void build() {
        new ComponentFactory().active();
    }
}
