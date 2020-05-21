package personal.popy.localserver.support;

import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfigration {

    @Bean
    public ServletWebServerFactory webFactory() {
        return new personal.popy.localserver.support.WebFactory();
    }
}
