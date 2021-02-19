import org.junit.Test;
import sun.nio.cs.ext.GBK;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class ClientApplicationTests {


    public static void main(String[] args) throws Exception {
        Charset charset= new GBK();
    }



    @Test
    public void runs() throws Exception {
        System.out.println(new Sio((short) 0x600F));
    }

    private static int getInt(ByteBuffer b) {
        int length = b.limit();
        int ret = 0;
        for (int i=0;i<length;i++) {
            byte bb = b.get();
            ret = (ret << 8) | (bb&0xff);
        }
        return ret;
    }


}

