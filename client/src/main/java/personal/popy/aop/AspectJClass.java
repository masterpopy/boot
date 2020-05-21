package personal.popy.aop;

import org.springframework.stereotype.Component;

@Component
public class AspectJClass {
	int a = 2;

	@HyAspect
	public String print(String args){
		System.out.println(a+args);
		return "1";
	}
}
