package personal.popy.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import personal.popy.entity.User;

import javax.annotation.PostConstruct;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexAction extends BaseController {
    private static final Log log = LogFactory.getLog(IndexAction.class);
    private byte[] data;

    private int length;

    @PostConstruct
    public void init() throws Exception {
        data = new byte[1024 * 8];
        length = getClass().getResourceAsStream("/static/abc.html").read(data, 0, data.length);
    }

    @GetMapping("index")
    @ResponseBody
    public void hello(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AsyncContext asyncContext = request.startAsync();
        response.getOutputStream().setWriteListener(new WriteListener() {
            private int cnt;

            @Override
            public void onWritePossible() throws IOException {
                while (response.getOutputStream().isReady() && cnt++ < 100) {
                    response.getOutputStream().write(data, 0, length);
                }
                if (cnt >= 100) {
                    asyncContext.complete();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @GetMapping("index2")
    @ResponseBody
    public void hello2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = response.getOutputStream();

        for (int i = 0; i < 100; i++) {
            outputStream.write(data, 0, length);
        }
    }


    @PostMapping("post")
    @ResponseBody
    public User post(@RequestBody User usr) {

        return usr;
    }

   /* @GetMapping("cal")
    public void cal(HttpServletResponse response) throws IOException {
        TimeCal read = new TimeCal();
        TimeCal servlet = new TimeCal();
        TimeCal write = new TimeCal();
        HttpProcessor.stack.each((s) -> {
            servlet.add(s.getRequest().getTime());
            read.add(s.getTime());
            write.add(((BlockRespWriter) s.getTask()).getTime());
        });
        ExecutorService worker = ((ResponseImpl) response).getHttpExchanger().getServer().getConnectionContext().getWorker();

        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        String cal = "平均read时间： " + read.get() +
                ", 平均servlet时间：" + servlet.get() +
                ", 平均阻塞write： " + write.get();
        response.getWriter().println(cal);
    }*/


}
