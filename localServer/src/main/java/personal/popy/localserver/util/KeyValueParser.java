package personal.popy.localserver.util;

public class KeyValueParser {

    private char kvSep;
    private char pairSep;
    private StringBuilder buffer = new StringBuilder();

    public KeyValueParser(char kvSep, char pairSep) {
        this.kvSep = kvSep;
        this.pairSep = pairSep;
    }

    public void parse(String s) {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if (Character.isSpaceChar(c)) {
                continue;
            }
            if (c == kvSep) {

            } else if (c == pairSep) {

            } else {
                buffer.append(c);
            }


        }
    }
}
