package personal.popy.action;

import org.springframework.beans.factory.BeanNameAware;

public abstract class BaseController implements BeanNameAware {
	@Override
	public void setBeanName(String s) {
		System.out.println(s);
	}
}
