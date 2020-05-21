package personal.popy.record.xse;

public class StringLable extends Lable {
    private StringBuilder sb;

    static final byte TERM = (byte) 0xFF;
    static final char LINE = (char) 0xFE;
    static final char PAGE = (char) 0XFA;

    public StringLable() {
    }

    public void getBytes(ArrayBuffer buffer) {
        char[] c = new char[sb.length()];
        sb.getChars(0, c.length, c, 0);
        StringEncoder.encode(c, buffer);
        buffer.addByte(TERM);
    }

    public StringLable text(String s) {
        sb = new StringBuilder().append(s);
        return this;
    }


    public StringLable line(String s) {
        append(s, LINE);
        return this;
    }

    public StringLable page(String s) {
        append(s, PAGE);
        return this;
    }

    private void append(String s, char c) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(c).append(s);
    }


}
