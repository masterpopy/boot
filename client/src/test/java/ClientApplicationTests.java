import org.junit.Test;

import java.nio.ByteBuffer;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class ClientApplicationTests {


    public static void main(String[] args) throws Exception {

    }



    @Test
    public void runs() throws Exception {

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

