package personal.popy.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;
import personal.popy.entity.Dept;
import personal.popy.localserver.lifecycle.HttpProcessor;
import personal.popy.localserver.servlet.HttpExchanger;
import personal.popy.localserver.util.TimeCal;
import personal.popy.service.DeptService;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Controller
@RequestMapping("action")
public class IndexAction extends BaseController {
    @Autowired
    DeptService service;

    public String getOne() {
        return "one";
    }

    @GetMapping("index")
    @ResponseBody
    public Map hello(String obj) {
        Map result = new HashMap();
        for (int i = 0; i < 2; i++) {
            result.put(i, obj);
        }
        return result;
    }

    @GetMapping("cal")
    public void cal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TimeCal read = new TimeCal();
        TimeCal servlet = new TimeCal();
        HttpProcessor.stack.each((s) -> {
            servlet.add(s.getRequest().getTime());
            read.add(s.getTime());
        });
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        String cal = "平均read时间： " + read.get(HttpExchanger.suc.get()) + ", 平均servlet时间：" + servlet.get(HttpExchanger.suc.get());
        response.getWriter().println(cal);
    }

    @RequestMapping("dept/{id}")
    @ResponseBody
    public Dept queryDept(@PathVariable int id) {
        System.out.println(service.getClass());
        System.out.println(service.mapper.getClass().getResource("/"));
        //return service.select(id);
        return null;
    }

    @RequestMapping("exception")
    @ResponseBody
    public Map testFunction2(@RequestParam HashMap map) {
        throw new RuntimeException("sgaklgn");
    }

    @RequestMapping("string")
    public void returnString(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext asyncContext = request.startAsync();
        Runnable runnable = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = new HashMap<>();
            map.put("threadName", Thread.currentThread().getName());
            map.put("time", new Date().toString());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            try {
                mapper.writeValue(response.getWriter(), map);
            } catch (IOException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        };
        asyncContext.start(runnable);
    }

    @RequestMapping("strings")
    @ResponseBody
    public WebAsyncTask<HashMap> getAsync() {
        Callable<HashMap> callable = () -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("threadName", Thread.currentThread().getName());
            map.put("time", new Date().toString());
            return map;
        };
        return new WebAsyncTask<>(callable);
    }


}
