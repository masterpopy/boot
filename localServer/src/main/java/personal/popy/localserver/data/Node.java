package personal.popy.localserver.data;

import java.util.Map;

public class Node implements Comparable<Node>, Map.Entry<String, Object> {
    Node(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String name;
    public Object value;

    @Override
    public int compareTo(Node node) {
        return name.compareToIgnoreCase(node.name);
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
