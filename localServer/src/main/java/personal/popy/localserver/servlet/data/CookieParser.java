package personal.popy.localserver.servlet.data;

import personal.popy.localserver.util.KeyValueParser;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

public class CookieParser {
    public static final String DOMAIN = "$Domain";
    public static final String VERSION = "$Version";
    public static final String PATH = "$Path";

    /**
     * 支持的格式  key=value; key=value
     *
     * @param s cookie字符串
     * @return Cookie数组
     */
    public static Cookie[] parse(String s) {
        ArrayList<Cookie> cookies = new ArrayList<>();
        KeyValueParser parser = new KeyValueParser(s);
        String domain = null;
        int version = 0;
        String path = null;
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
                        domain = value;
                        break;
                    case VERSION:
                        version = Integer.parseInt(value);
                        break;
                    case PATH:
                        path = value;
                        break;
                    default:
                        cookies.add(new Cookie(key, value));
                        break;
                }
                continue;
            }

            cookies.add(new Cookie(key, value));
        }

        int size = cookies.size();
        Cookie[] ret = new Cookie[size];
        for (int i = 0; i < size; i++) {
            Cookie cookie = cookies.get(i);
            if (domain != null) {
                cookie.setDomain(domain);
            }
            cookie.setPath(path);
            cookie.setVersion(version);
            ret[i] = cookie;
        }
        return ret;
    }


}
