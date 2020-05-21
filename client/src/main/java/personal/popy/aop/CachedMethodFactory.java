package personal.popy.aop;

import org.springframework.stereotype.Component;
import personal.popy.datastructure.LazySoftRef;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CachedMethodFactory {
	private LazySoftRef<ConcurrentHashMap<Method, MethodInfo>> proxycache =
			new LazySoftRef<>(ConcurrentHashMap::new);

	public <T> T newInstance(Class<T> interfaces, ParameterMap target) {
		return (T) Proxy.newProxyInstance(CachedMethodFactory.class.getClassLoader(),
				new Class[]{interfaces}, new MethodProxy(proxycache, target));
	}

	public <T> T newInstance(Class<T> in) {
		return newInstance(in, (info, objects) -> {
			Object o = info.getParameterMap(objects);
			System.out.println(o);
			return o;
		});
	}
}
