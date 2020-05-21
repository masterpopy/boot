package personal.popy.aop;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
@Configuration
@ConditionalOnClass({HyRepository.class})
@Import(MyBeanRegistrar.class)
public class MyBeanConfiguration {
}
