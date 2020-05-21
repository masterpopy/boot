package personal.popy.aop;

import personal.popy.datastructure.LazySoftRef;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class MethodProxy implements InvocationHandler {

	protected LazySoftRef<ConcurrentHashMap<Method, MethodInfo>> proxycache;
	protected ParameterMap target;

	public MethodProxy(LazySoftRef<ConcurrentHashMap<Method, MethodInfo>> proxycache,
	                   ParameterMap target) {
		this.proxycache = proxycache;
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}
		if(method.isDefault()){

		}
		MethodInfo m = proxycache.get().computeIfAbsent(method, MethodInfo::new);
		return target.accept(m, args);
	}
}
