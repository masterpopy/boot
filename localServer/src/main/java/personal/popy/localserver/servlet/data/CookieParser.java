package personal.popy.localserver.servlet.data;

import personal.popy.localserver.util.KeyValueParser;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class CookieParser {
    public static final String DOMAIN = "$Domain";
    public static final String VERSION = "$Version";
    public static final String PATH = "$Path";

    /**
     * 支持的格式  key=value; key=value
     * @param s cookie字符串
     * @return Cookie数组
     */
    public static Cookie[] parse(String s) {
        HashMap<String, String> map = new HashMap<>();
        KeyValueParser parser = new KeyValueParser(s);
        $1 base = new $1("null", "null");
        while (parser.hasMore()) {
            parser.skipWhiteSpace();
            String key = parser.nextToken('=');

            parser.skipWhiteSpace();
            String value = parser.nextToken(';');
            if (key.length() == 0) {
                continue;
            }
            if (value.length() > 0) {
                switch (key) {
                    case DOMAIN:
                        base.setDomain(value);
                        break;
                    case VERSION:
                        base.setVersion(Integer.parseInt(value));
                        break;
                    case PATH:
                        base.setPath(value);
                        break;
                    default:
                        map.put(key, value);
                        break;
                }
                continue;
            }

            map.put(key, value);
        }

        int size = map.size();
        int index = 0;
        Cookie[] ret = new Cookie[size];
        for (Map.Entry<String, String> entry : map.entrySet()) {
            ret[index++] = base.toCookie(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    private static class $1 extends Cookie {

        public $1(String name, String value) {
            super(name, value);
        }

        @Override
        public String toString() {
            return getName() + "=" + getValue();
        }

        private $1 toCookie(String name, String value) {
            $1 cookie = new $1(name, value);
            String domain = getDomain();
            if (domain != null)
                cookie.setDomain(domain);
            cookie.setPath(getPath());
            cookie.setVersion(getVersion());
            return cookie;
        }
    }
}
