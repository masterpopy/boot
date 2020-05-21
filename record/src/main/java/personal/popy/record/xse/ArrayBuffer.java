package personal.popy.record.xse;

import static personal.popy.record.xse.En.HEX;

public class ArrayBuffer {
    byte[] bytes;
    char[] chars;


    int byteIndex;
    int charIndex;

    public ArrayBuffer(int b, int c) {
        bytes = new byte[b];
        chars = new char[c];
    }


    public ArrayBuffer append(String str) {
        str.getChars(0, str.length(), chars, charIndex);
        charIndex += str.length();
        return this;
    }

    public ArrayBuffer append(char c) {
        chars[charIndex++] = c;
        return this;
    }

    public ArrayBuffer appendHex(byte c) {
        append("0x").append(HEX[(c & 0xF0) >> 4]).append(HEX[c & 0xF]);
        return this;
    }

    public String getStrAndReset() {
        return getStrAndReset(charIndex);
    }

    public String getStrAndReset(int length) {
        charIndex = 0;
        if (length == 0) {
            return "";
        }
        return new String(chars, 0, length);
    }


    public void setCharIndex(int i) {
        charIndex = i;
    }

    public void append(int value) {

        if (value == 0) {
            append("0x00");
            return;
        }
        append("0x");
        int mask = 28;

        while (mask >= 0) {
            int v = (value >>> mask) & 0xF;
            if (v != 0) break;
            mask -= 4;
        }

        while (mask >= 0) {
            int v = (value >>> mask) & 0xF;
            append(HEX[v]);
            mask -= 4;
        }
    }

    public void addByte(byte term) {
        bytes[byteIndex++] = term;
    }

    public void addBytes(byte[] data) {
        System.arraycopy(data, 0, bytes, byteIndex, data.length);
        byteIndex += data.length;
    }

    public String getBytesHex() {
        for (int i = 0; i < byteIndex; i++) {
            appendHex(bytes[i]).append(", ");
        }
        byteIndex = 0;
        return getStrAndReset(charIndex - 2);
    }
}
