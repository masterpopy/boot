package personal.popy.aop;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class HyRepositoryFactoryBean implements FactoryBean {

	Class interfaceClass;
	Object result;

	public HyRepositoryFactoryBean(String classname) throws ClassNotFoundException {
		this.interfaceClass = Class.forName(classname);
	}

	@Override
	public Object getObject() {
		if (result == null) {
			result = create();
		}
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

	public Object create() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(AbstractClass.class);
		enhancer.setInterfaces(new Class[]{interfaceClass});
		enhancer.setCallback(getCallback());
		return enhancer.create();
	}

	public MethodInterceptor getCallback() {
		Type[] interfaces = interfaceClass.getGenericInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			ParameterizedType type = (ParameterizedType) interfaces[i];
			if (type.getRawType() == HyRepository.class) {
				System.out.println(type.getActualTypeArguments()[0]);
				break;
			}
		}
		return (o, method, objects, methodProxy) -> {
			System.out.println("正在拦截方法：" + method);
			return null;
		};
	}
}
