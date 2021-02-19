import org.junit.Test;
import personal.popy.record.structure.AbstractQueueSync;
import personal.popy.record.xse.IpsScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class RecordTest {


    @org.junit.Test
    public void disruptorTest() {
        AbstractQueueSync aqs = new AbstractQueueSync();
        aqs.add(1);
        aqs.add(2);
        aqs.add(3);
        aqs.add(4);

        System.out.println(aqs.get());
        System.out.println(aqs.get());

        aqs.add(1);
        aqs.add(2);
        aqs.add(3);
        aqs.add(4);
    }

    private class newA{
        public newA() {
            throw new RuntimeException();
        }
    }

    @Test
    public void runAlgorithms() throws Exception {
        new newA();
    }

    @Test
    public void runArmIps() throws Exception {
        String rom = "E:/QQ/File/Truck Remover/test.gba";
        String path = getClass().getClassLoader().getResource("").getFile();
        String output = path + "script.txt";

        IpsScript arm = new IpsScript(rom, output);

        arm.generate();
        arm.runArmips();
        arm.runGba();
    }

    @Test
    public void runEncode() throws Exception {
        Properties p = new Properties();
        String code = "完整码表.txt";
        p.load(new InputStreamReader(getClass().getResourceAsStream(code), StandardCharsets.UTF_8));
        byte[] bytes = new byte[0xfffff];
        for (Map.Entry<Object, Object> entry : p.entrySet()) {
            String data = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (value == null || value.length() == 0)
                continue;
            int d = Integer.parseInt(data, 16);
            if (d == 0x05F7) {
                System.out.println(1);
            }
            char ch = value.charAt(0);
            if (!(ch >= 0x4e00 && ch <= 0x9fa5)) {
                continue;
            }

            int c = ch * 2;
            bytes[c] = (byte) (d >> 8);
            bytes[c + 1] = (byte) (d & 0xFF);


        }

        byte hi = bytes['奇'];
        byte lo = bytes['奇' + 1];
        System.out.println(Integer.toHexString(hi));
        System.out.println(Integer.toHexString(lo));
        int start = 0;
        int end = bytes.length - 1;

        while (start < end) {
            if (bytes[start] == 0) {
                start++;
            } else {
                break;
            }
        }

        while (start < end) {
            if (bytes[end] == 0) {
                end--;
            } else {
                break;
            }
        }
        System.out.println(start);
        System.out.println(end);

        System.out.println(Arrays.toString(Arrays.copyOfRange(bytes, start, end + 1)));
        String out = "encode.bin";
        String path = getClass().getResource(out).getPath();
        new FileOutputStream(path).write(bytes, start, end + 1);
    }

    static void setShort(byte[] value, int index, char ch) {
        index *= 2;
        value[index] = (byte) ch;
        value[index + 1] = (byte) (ch >> 8);
    }

    @Test
    public void runDecode() throws Exception {
        String code = "完整码表.txt";
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(code), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        String line;
        byte[] bytes = new byte[(0x1E5E - 0x100 + 1) * 2];
        while ((line = br.readLine()) != null) {
            int index = line.indexOf('=');
            if (index < line.length() - 1) {
                int intValue = Integer.parseInt(line.substring(0, index), 16) - 0x100;
                char charValue = line.charAt(index + 1);
                setShort(bytes, intValue, charValue);
            }
        }
        String out = "decode.bin";
        String path = getClass().getResource(out).getPath();
        new FileOutputStream(path).write(bytes, 0, bytes.length);
    }


}
