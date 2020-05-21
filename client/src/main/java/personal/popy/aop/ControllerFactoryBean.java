package personal.popy.aop;

import org.springframework.beans.factory.FactoryBean;

public class ControllerFactoryBean implements FactoryBean {

	Class interfaceClass;
	Class classname;
	Object result;

	public ControllerFactoryBean(String interfaceClass,String classname) throws ClassNotFoundException {
		this.interfaceClass = Class.forName(interfaceClass);
		this.classname = Class.forName(classname);
	}

	@Override
	public Object getObject() throws Exception {
		if(result == null) return result = classname.newInstance();
		return result;
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
