package personal.popy.localserver.data;

import personal.popy.localserver.util.BufferUtil;

import javax.servlet.http.Cookie;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Headers extends HeaderList<String> {
    private List<Cookie> cookies;

    public void addCookie(Cookie cookie) {
        if (cookie == null) {
            cookies = new ArraySet<>();
        }
        cookies.add(cookie);
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void getChars(ByteBuffer cb) {
        for (int i = 0; i < size; i++) {
            Node node = table[i];
            BufferUtil.put(cb, node.name);
            cb.put((byte) ':');
            BufferUtil.put(cb, (String) node.value);
            cb.put(BufferUtil.LINE_SEP);
        }
        cb.put(BufferUtil.LINE_SEP);
    }

    static class ValueList extends AbstractList<String> {
        ValueList(int start, int end, Node[] table) {
            this.start = start;
            this.end = end;
            this.size = end - start;
            this.table = table;
        }

        int start;
        int end;
        int size;
        Node[] table;

        @Override
        public String get(int index) {
            return (String) table[start + index].value;
        }

        @Override
        public int size() {
            return size;
        }
    }


    public Collection<String> getHeaderValues(String header) {
        sort();
        int i = Arrays.binarySearch(table, 0, size, new Node(header, null));
        if (i < 0) {
            return Collections.emptyList();
        }
        int start = i;
        int end = i;
        while (end < size) {
            if (!table[++end].keyEq(header)) {
                break;
            }
        }
        while (start > 0) {
            if (table[start - 1].keyEq(header)) {
                start--;
            } else {
                break;
            }
        }
        return new ValueList(start, end, table);
    }
}
