package personal.popy.datastructure;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class LazySoftRef<T> {

	private volatile SoftReference<T> holder;

	private Supplier<T> supplier;

	private final AtomicInteger lock = new AtomicInteger();

	public LazySoftRef(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public T get() {//初始化操作
		T obj;SoftReference<T> hld;
		while ((hld = this.holder) == null || (obj = hld.get()) == null) {
			if (lock.intValue() == 1)
				Thread.yield();
			else if (lock.compareAndSet(0, 1)) {
				try {
					if ((hld = this.holder) != null && (obj = hld.get()) != null) {
						return obj;
					}
					obj = supplier.get();
					holder = new SoftReference<>(obj);
				} finally {
					lock.set(0);
				}
			}
		}
		return obj;
	}
}
