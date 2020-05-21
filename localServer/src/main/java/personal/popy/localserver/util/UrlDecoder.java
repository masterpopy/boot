package personal.popy.localserver.util;

import personal.popy.localserver.exception.ServerException;
import personal.popy.localserver.wrapper.HttpReqEntity;
import personal.popy.localserver.wrapper.SliencedBuffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;

public class UrlDecoder {
    public static void convert(SliencedBuffer str, CharBuffer cb, CharsetDecoder decoder, HttpReqEntity entity) {
        int i = str.getStart();//当前指针位置
        final int limit = str.getLimit();
        byte[] origin = str.getOrigin();
        int pos = i;//填充指针位置
        ByteBuffer wrap = ByteBuffer.wrap(origin, pos, pos);
        String key = null;
        while (i < limit) {
            char c = (char) origin[i];
            switch (c) {
                case '%':
                    if (i + 3 <= limit) {
                        int v = x2c(origin[i + 1], origin[i + 2]);
                        if (v < 0) {
                            throw new ServerException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
                        }
                        origin[pos++] = (byte) v;
                        i += 3;
                    } else {
                        throw new ServerException("URLDecoder: Incomplete trailing escape (%) pattern");
                    }
                    break;
                case '+':
                    cb.put(' ');
                    ++i;
                    break;
                case '=':
                    key = convert(wrap, pos, cb, decoder);
                    pos = 0;
                    ++i;
                    break;
                case '&':
                    String value = convert(wrap, pos, cb, decoder);
                    entity.parameters.add(key, value);
                    pos = 0;
                    ++i;
                    break;
                default:
                    cb.put(c);
                    ++i;
            }
        }
        String value = convert(wrap, pos, cb, decoder);
        entity.parameters.add(key, value);
    }

    private static String convert(ByteBuffer wrap, int pos, CharBuffer cb, CharsetDecoder decoder) {
        if (pos > 0) {
            wrap.limit(pos);
            //todo buffer full
            decoder.decode(wrap, cb, false);
        }
        String x = new String(cb.array(), 0, cb.position());
        cb.clear();
        return x;
    }


    private static int x2c(byte b1, byte b2) {
        if (isNotHexDigit(b1) || isNotHexDigit(b2)) {
            throw new ServerException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
        }
        int digit = (b1 >= 'A') ? ((b1 & 0xDF) - 'A') + 10 :
                (b1 - '0');
        digit *= 16;
        digit += (b2 >= 'A') ? ((b2 & 0xDF) - 'A') + 10 :
                (b2 - '0');
        return digit;
    }


    private static boolean isNotHexDigit(int c) {

        return ((c < '0' || c > '9') &&
                (c < 'a' || c > 'f') &&
                (c < 'A' || c > 'F'));
    }
}
