package personal.popy.record.xse;

import static personal.popy.record.xse.AsmGenerator.*;

public class WalkLable extends StringLable {

    private byte[] data;

    public WalkLable(byte... data) {
        this.data = data;
    }

    @Override
    public void getBytes(ArrayBuffer buffer) {
        buffer.addBytes(data);
        buffer.addByte((byte) 0xFE);
    }

    public static byte[] fromString(String movement) {
        byte[] data = new byte[movement.length()];

        for (int i = 0; i < data.length; i++) {
            byte b;
            switch (movement.charAt(i)) {
                case '>':
                    b = RIGHT_FAST;
                    break;
                case '<':
                    b = LEFT_FAST;
                    break;
                case '^':
                    b = UP_FAST;
                    break;
                case 'v':
                    b = DOWN_FAST;
                    break;
                case '!':
                    b = EXCLAM;
                    break;
                default:
                    throw new IllegalArgumentException("移动不支持的字符：" + movement.charAt(i));
            }
            data[i] = b;
        }
        return data;
    }

}
