package personal.popy.localserver.factory;

public class ComponentFactory implements FactoryInitializer {
    private FactoryInitializer delegate;
    private WebResourceLoader webResource;
    private String basePackage;

    public void setDelegate(FactoryInitializer delegate) {
        this.delegate = delegate;
    }

    public Object buildObj(){
        return null;
    }

    @Override
    public void active() {
        delegate.active();
    }

    @Override
    public void refresh() {
        delegate.refresh();
    }

    @Override
    public void destory() {
        delegate.destory();
    }
}
