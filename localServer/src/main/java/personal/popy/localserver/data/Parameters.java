package personal.popy.localserver.data;

import java.util.HashMap;
import java.util.Map;

public class Parameters extends FastList<String> {

    private Map<String, String[]> pm;
    private boolean computed;

    private void createPm() {
        if (pm == null) {
            pm = new HashMap<>(size / 3 * 4);
        }
    }

    public String[] getParameterValues(String s) {
        if (computed) {
            return pm.get(s);
        }
        sort();
        createPm();
        for (int i = 0; i < size; i += 1) {
            if (table[i].keyEq(s)) {
                return createPmValues(i, s);
            }
        }
        return null;
    }

    private String[] createPmValues(int i, String name) {
        int cnt = 1;
        int pos = i;
        while (++i < size) {
            if (table[i].keyEq(name)) {
                cnt++;
            } else {
                break;
            }
        }
        int n = cnt;
        return pm.computeIfAbsent(name, s -> {
            String[] ret = new String[n];
            for (int j = 0; j < n; j++) {
                ret[j] = (String) table[pos + j].value;
            }
            return ret;
        });
    }

    public Map<String, String[]> parameters() {
        if (!computed) {
            sort();
            createPm();
            for (int i = 0; i < size; ) {
                String name = table[i].name;
                i += createPmValues(i, name).length;
            }
            computed = true;
        }

        return pm;
    }
}
