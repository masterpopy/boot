package personal.popy.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MethodInfo {
	protected Method method;
	private Parameter[] parameters;

	MethodInfo(Method method) {
		this.method = method;
		parameters = method.getParameters();
	}

	public String getName() {
		return method.getName();
	}

	public <T extends Map<String, Object>> T fillParameterMap(Object[] args, Supplier<T> supplier) {
		if (args.length == 0) return null;
		T map = supplier.get();
		for (int i = 0; i < parameters.length; i++) {
			map.put(parameters[i].getName(), args[i]);
		}
		return map;
	}

	public HashMap<String, Object> getParameterMap(Object[] args) {
		return fillParameterMap(args, HashMap::new);
	}

	public Method getMethod() {
		return method;
	}
}
