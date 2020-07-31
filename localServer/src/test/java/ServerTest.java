import org.junit.Test;
import personal.popy.localserver.servlet.util.RestPath;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.function.Consumer;

public class ServerTest {

    @Test
    public void test0() throws Exception {
        RestPath<String> path = new RestPath<>("wgasg");
        path.addPrefixPath("a/", "www");

        System.out.println(path.match("/a").getValue());

    }

    public static void et(Object s) {

    }

    @Test
    public void test1() throws Exception {
        SSLContext sslContext=SSLContext.getDefault();
        sslContext.getDefaultSSLParameters().setCipherSuites(new String[]{"TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384"});
        SSLEngine sslEngine = sslContext.createSSLEngine();
        System.out.println(Arrays.toString(sslEngine.getEnabledCipherSuites()));
    }

    @Test
    public void test2() throws Exception {

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
