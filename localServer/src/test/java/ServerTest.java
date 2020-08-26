import org.junit.Test;
import personal.popy.localserver.servlet.util.RestPath;
import personal.popy.localserver.util.UnSafeStrBuf;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
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
        UnSafeStrBuf cw = new UnSafeStrBuf();
        cw.append(835583).append('2').append("/gsagsag").append("sagasgasgwagwgawwgawg".toCharArray()).append(-515151525);

        System.out.println(cw);
    }

    @Test
    public void test2() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);
        Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
        // output pretty printed

        Root unmarshal = (Root) jaxbMarshaller.unmarshal(getClass().getResourceAsStream("root.xml"));
        UnSafeStrBuf buf=new UnSafeStrBuf();
        for (ValidOption validOption : unmarshal.validOptions) {
            System.out.println(buf.append("--").append(validOption.name).append("  ").append(validOption.explain).append("。").getAndReset());
        }
    }

    @Test
    public void test3() throws Exception {

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
