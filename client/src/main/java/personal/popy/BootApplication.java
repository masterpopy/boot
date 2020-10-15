package personal.popy;

import com.tongweb.srv.commons.log.config.LogConfigProvider;
import com.tongweb.srv.commons.log.config.appender.AppenderType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 *
 */
@SpringBootApplication
public class BootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(BootApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        LogConfigProvider.generateAsyncLog(
                2, AppenderType.FILE.name(),
                200, null,null,null);
        return application.sources(BootApplication.class);
    }
}
