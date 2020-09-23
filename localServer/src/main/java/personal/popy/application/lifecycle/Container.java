package personal.popy.application.lifecycle;

import personal.popy.copy.spring.lang.NonNull;

import java.util.Collection;
import java.util.HashMap;

public abstract class Container implements Lifecycle {
    private String scope;

    private final Container parent;

    private HashMap<String, String> prop;

    protected Container(Container parent) {
        this.parent = parent;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setProperty(String key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (value == null) {
            value = "";
        }
        if (prop == null) {
            prop = new HashMap<>();
        }
        prop.put(key, value);
    }

    @NonNull
    public String getProperty(String key) {
        if (prop != null) {
            String s = prop.get(key);
            if (s != null) {
                return s;
            }
        }
        if (parent != null) {
            return parent.getProperty(key);
        }
        return "";
    }

    public void addEndpoint(BeanDef<?> bean) {

    }

    public void setEntrypoint(BeanDef<?> bean) {

    }

    public Entrypoint getEntrypoint() {
        return null;
    }

    public void contact(Container another) {
        getEntrypoint().contact(another.getEntrypoint());
    }

    public void addChild(Container container) {

    }

    public Collection<? extends Container> getChild() {
        return null;
    }


}
