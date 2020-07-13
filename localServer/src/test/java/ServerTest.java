import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerTest {

    @Test
    public void test0() throws Exception {
        RingBuffer buffer = RingBuffer.createSingleProducer(null,200);
        SequenceBarrier seq = buffer.newBarrier(null);

    }

    public static void et(Object s) {

    }

    @Test
    public void test1() {
        try (Socket socket = new Socket("localhost", 8080);
             OutputStream stream = socket.getOutputStream()) {
            String shutdown = "method url protocol\r\nInt=Ws\r\nWG=sgag\r\n";
            stream.write(shutdown.getBytes());
            stream.flush();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws Exception {
        Matcher matcher = Pattern.compile("0mw\\S*?</td>").matcher("");
        String[] files = {
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-ejb\\要删除列表\\FOSSID.html",
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-javassist\\要删除列表\\FOSSID.html",
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-jca\\要删除列表\\FOSSID.html",
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-jdbc\\要删除列表\\FOSSID.html"
        };
        for (String fileName : files) {
            System.out.println();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.lines().forEach(s -> {
                matcher.reset(s);
                while (matcher.find()) {
                    String group = matcher.group();
                    group = group.substring("0mw/0-13M-13W/".length(), group.length() - 5);
                    System.out.println(group);
                }
            });
        }
    }

    @Test
    public void test3() throws Exception {

        String path = "C:\\Users\\lihq\\Desktop\\file to delete";
        BufferedReader br = new BufferedReader(new FileReader(path));
        Cnt action = new Cnt();
        br.lines().forEach(action);
        System.out.println(action.cnt);
    }

    private static class Cnt implements Consumer<String> {
        int i = 0;
        int cnt = 0;
        String[] files = {
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-ejb\\src\\",
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-javassist\\src\\",
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-jca\\src\\",
                "C:\\Users\\lihq\\Documents\\WeChat Files\\popy596963\\FileStorage\\File\\2020-04\\要删除的列表\\6-jdbc\\src\\"
        };

        @Override
        public void accept(String s) {
            if (s.length() == 0) {
                i++;
                return;
            }
            if (new File(files[i] + s).delete()) {
                cnt++;
            }
        }
    }
}
