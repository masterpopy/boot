package personal.popy.localserver.data;

import java.util.Map;

public class Node implements Comparable<Node>, Map.Entry<String, Object> {
    Node(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    String name;
    Object value;

    @Override
    public int compareTo(Node node) {
        int ret = name.compareToIgnoreCase(node.name);
        if (ret == 0) {
            name = node.name;
        }
        return ret;
    }

    boolean keyEq(String n) {
        return name.equalsIgnoreCase(n);
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
        return this.value = value;
    }
}
