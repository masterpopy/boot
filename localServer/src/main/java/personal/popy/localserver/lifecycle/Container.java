package personal.popy.localserver.lifecycle;

import personal.popy.copy.spring.lang.NonNull;

import java.util.HashMap;
import java.util.List;

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

    public <T> T getBean(Class<T> beanClass) {
        return null;
    }

    public <T> List<T> getBeans(Class<T> beanClass) {
        return null;
    }

    public Object getBean(String name) {
        return null;
    }

    public <T> T getBean(String name, Class<T> beanClass) {
        return null;
    }
}
