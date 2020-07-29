package personal.popy.localserver.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Parameters extends FastList<String> {

    public String[] getParameterValues(String s) {
        sort();
        for (int i = 0; i < size; i += 1) {
            if (table[i].keyEq(s)) {
                return createValueArray(i, s);
            }
        }
        return null;
    }

    private String[] createValueArray(int i, String name) {
        int cnt = 1;
        int pos = i;
        while (++i < size) {
            if (table[i].keyEq(name)) {
                cnt++;
            } else {
                break;
            }
        }
        String[] ret = new String[cnt];
        for (int j = 0; j < cnt; j++) {
            ret[j] = (String) table[pos + j].value;
        }
        return ret;
    }

    public Map<String, String[]> parameters() {
        sort();
        if (size == 0) {
            return Collections.emptyMap();
        }
        Map<String, String[]> pmCache = new HashMap<>(size / 3 * 4);
        for (int i = 0; i < size; ) {
            String name = table[i].name;
            String[] pmValues = createValueArray(i, name);
            i += pmValues.length;
            pmCache.put(name, pmValues);
        }
        return pmCache;
    }
}
