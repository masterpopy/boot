import org.junit.Test;
import personal.popy.copy.spring.io.Resource;
import personal.popy.copy.tomcat.classfile.ClassParser;
import personal.popy.copy.tomcat.classfile.JavaClass;
import personal.popy.server.factory.ClassResourceLoader;
import personal.popy.server.io.ReusableBufferedInputStream;
import personal.popy.server.util.UnSafeStrBuf;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

public class ServerTest {

    @Test
    public void test0() throws Exception {
        Resource[] a = new ClassResourceLoader().find("personal.popy.localserver");
        ReusableBufferedInputStream bufferedInputStream = new ReusableBufferedInputStream(null);
        for (Resource resource : a) {
            InputStream inputStream = resource.getInputStream();
            bufferedInputStream.setIn(inputStream);
            JavaClass parse = new ClassParser(bufferedInputStream).parse();
            inputStream.close();
            System.out.println(parse.getClassName());
        }
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
        String.class.getDeclaredField("value").setAccessible(true);
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
