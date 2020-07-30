package personal.popy.localserver.io;

import java.util.IdentityHashMap;

public class IoEnv {
    private InputBuffer inputBuffer;
    private IdentityHashMap<AttrKey<?>, Object> attributes;

    public InputBuffer inputBuffer() {
        return inputBuffer;
    }

    public IoEnv inputBuffer(InputBuffer buffer) {
        this.inputBuffer = buffer;
        return this;
    }

    public <T> T attr(AttrKey<T> key) {
        Object o = attributes.get(key);
        return key.cast(o);
    }

    public <T> void attr(AttrKey<T> key, T value) {
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
