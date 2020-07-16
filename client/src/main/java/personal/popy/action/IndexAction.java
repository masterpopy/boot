package personal.popy.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("action")
public class IndexAction extends BaseController {

    @PostMapping("index")
    @ResponseBody
    public String hello(@RequestBody String hash) {

        System.out.println(hash);
        return "123";
    }

    @GetMapping("index2")
    @ResponseBody
    public String hello2() {

        return "123";
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
