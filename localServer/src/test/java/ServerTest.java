import org.junit.Test;
import personal.popy.localserver.servlet.util.RestPath;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.StringTokenizer;
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

    }

    @Test
    public void test2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
        Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
        // output pretty printed

        Root unmarshal = (Root) jaxbMarshaller.unmarshal(getClass().getResourceAsStream("root.xml"));
        for (ValidOption validOption : unmarshal.validOptions) {
            System.out.print(validOption.name+"={$"+validOption.name+"},");
        }
    }

    @Test
    public void test3() throws Exception {
        StringTokenizer tokenizer = new StringTokenizer("22/33/44/55", "/");
        while (tokenizer.hasMoreElements()) {
            System.out.println(tokenizer.nextElement());
        }
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
