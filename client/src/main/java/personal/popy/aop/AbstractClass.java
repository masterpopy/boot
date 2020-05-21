package personal.popy.aop;

import java.lang.reflect.Type;

public class AbstractClass<T> implements Cloneable,HyRepository<T> {

	@Override
	public final String toString(){
		return super.toString();
	}

	@Override
	public final int hashCode() {
		return super.hashCode();
	}

	@Override
	public final boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public final Object clone()  {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public final T findOne() {
		Type type = this.getClass().getGenericSuperclass();
		System.out.println(type);
		return null;
	}
}
