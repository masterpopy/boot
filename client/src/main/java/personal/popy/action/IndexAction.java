package personal.popy.action;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import personal.popy.localserver.connect.io.BlockRespWriter;
import personal.popy.localserver.lifecycle.HttpProcessor;
import personal.popy.localserver.servlet.ResponseImpl;
import personal.popy.localserver.util.TimeCal;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Controller
@RequestMapping("action")
public class IndexAction extends BaseController {

    @GetMapping("index")
    @ResponseBody
    public Map hello(String obj) {
        Map result = new HashMap();
        for (int i = 0; i < 200; i++) {
            result.put(i, obj);
        }
        return result;
    }

    @GetMapping("cal")
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
    }




}
