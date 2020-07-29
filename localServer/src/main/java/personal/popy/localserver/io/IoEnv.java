package personal.popy.localserver.io;

import java.util.IdentityHashMap;

public class IoEnv {
    private InputBuffer inputBuffer;
    private IdentityHashMap<String, Object> attributes;

    public InputBuffer inputBuffer() {
        return inputBuffer;
    }

    public IoEnv inputBuffer(InputBuffer buffer) {
        this.inputBuffer = buffer;
        return this;
    }

    public Object attr(String key) {
        return attributes.get(key);
    }

    public void attr(String key, Object value) {
        if (attributes == null) {
            attributes = new IdentityHashMap<>();
        }
        attributes.put(key, value);
    }

    public void cancel(){

    }

    public void setState() {

    }

}
