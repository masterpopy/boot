package personal.popy.localserver.data;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

public class HeaderList<V> {

    Node[] table;
    int size;
    private boolean sorted;

    public HeaderList(int i) {
        table = new Node[i];
    }

    public HeaderList() {
        this(16);
    }

    public V get(Object key) {
        return get((String) key);
    }

    public void remove(String name) {
        for (int i = 0; i < size; i++) {
            if (table[i].keyEq(name)) {
                table[i] = null;
                System.arraycopy(table, i, table, i + 1, size - i);
                --size;
                --i;
            }
        }
    }


    private Node newNode(String key, V value) {
        return new Node(key, value);
    }

    public final void add(String key, V value) {
        Objects.requireNonNull(key);
        sorted = false;
        resize();
        table[size] = newNode(key, value);
        size++;
    }

    private void resize() {
        if (size == table.length) {
            table = Arrays.copyOf(table, size * 2);
        }
    }

    public Node getNode(String key) {
        for (int i = 0; i < size; i += 1) {
            if (table[i].keyEq(key)) {
                return table[i];
            }
        }
        return Node.EMPTY;
    }

    public void clear() {
        if (size > 512) {
            table = new Node[16];//shorten it
        } else {
            for (int i = 0; i < size; i++) {
                table[i] = null;
            }
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private V get(String key) {
        for (int i = 0; i < size; i += 1) {
            if (table[i].keyEq(key)) {
                return (V) table[i].value;
            }
        }
        return null;
    }

    public void addIfNotExits(String key, V value) {
        if (get(key) != null) {
            throw new IllegalArgumentException();
        }
        add(key, value);
    }

    void sort() {
        if (!sorted) {
            sorted = true;
            Arrays.sort(table, 0, size);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(table);
    }

    public Enumeration<String> getHeaders(String header) {
        sort();
        return new HeaderEnumeration(header);
    }

    public Enumeration<String> names() {
        sort();
        return new NameEnumeration();
    }

    class HeaderEnumeration implements Enumeration<String> {
        private String h;
        private int index;
        private Node next;

        HeaderEnumeration(String h) {
            this.h = h;
        }

        @Override
        public boolean hasMoreElements() {
            for (; index < size; index += 1) {
                if (table[index].keyEq(h)) {
                    next = table[index];
                    return true;
                }
            }
            return false;
        }

        @Override
        public String nextElement() {
            if (next == null) {
                hasMoreElements();
            }
            if (next == null) {
                return null;
            }
            String next = (String) this.next.value;
            this.next = null;
            index++;
            return next;
        }
    }

    class NameEnumeration implements Enumeration<String> {
        private int index;

        NameEnumeration() {
        }

        @Override
        public boolean hasMoreElements() {
            return index < size;
        }

        @Override
        public String nextElement() {
            if (!hasMoreElements()) {
                return null;
            }
            String node = table[index].name;
            while (++index < size) {
                if (!table[index].keyEq(node)) {
                    break;
                }
            }
            return node;
        }
    }
}
