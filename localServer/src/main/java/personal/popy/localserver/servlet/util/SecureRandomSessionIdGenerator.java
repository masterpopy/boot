package personal.popy.localserver.servlet.util;

import java.security.SecureRandom;

public class SecureRandomSessionIdGenerator implements SessionIdGenerator {
    private final SecureRandom random = new SecureRandom();

    private volatile int length = 30;

    private static final char[] SESSION_ID_ALPHABET;

    private static final String ALPHABET_PROPERTY = "io.undertow.server.session.SecureRandomSessionIdGenerator.ALPHABET";

    static {
        String alphabet = System.getProperty(ALPHABET_PROPERTY, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_");
        if(alphabet.length() != 64) {
            throw new RuntimeException("io.undertow.server.session.SecureRandomSessionIdGenerator must be exactly 64 characters long");
        }
        SESSION_ID_ALPHABET = alphabet.toCharArray();
    }

    @Override
    public String createSessionId() {
        final byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new String(encode(bytes));
    }


    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * Encode the bytes into a String with a slightly modified Base64-algorithm
     * This code was written by Kevin Kelley <kelley@ruralnet.net>
     * and adapted by Thomas Peuss <jboss@peuss.de>
     *
     * @param data The bytes you want to encode
     * @return the encoded String
     */
    private char[] encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        char[] alphabet = SESSION_ID_ALPHABET;
        //
        // 3 bytes encode to 4 chars.  Output is always an even
        // multiple of 4 characters.
        //
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;

            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 63)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 63)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index] = alphabet[val & 0x3F];
        }
        return out;
    }
}
