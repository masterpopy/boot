package personal.popy.localserver.util;

public class KeyValueParser {

    private String value;

    private int pos;

    private int length;

    public KeyValueParser(String value) {
        this.value = value;
        this.length = value.length();
    }

    public boolean hasMore() {
        return pos < length;
    }

    public void skipWhiteSpace() {
        while (Character.isWhitespace(value.charAt(pos))) {
            pos++;
        }
    }

    public String nextToken(char delimeter) {
        int idx = pos;
        while (value.charAt(pos) != delimeter) {
            pos++;
            if (pos == length) {
                break;
            }
        }
        int pos = this.pos;
        this.pos++;//skip delimeter
        return value.substring(idx, pos);
    }
}
