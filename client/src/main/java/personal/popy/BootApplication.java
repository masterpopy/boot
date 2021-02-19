package personal.popy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;

/**
 *
 */
@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) throws Exception {
//        System.setSecurityManager(new NoExistManager());
        SpringApplication.run(BootApplication.class, args);
    }

}
