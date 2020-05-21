package personal.popy.entity;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleHashList<T> {
	protected volatile Object[] keys;
	protected volatile int count;
	private AtomicInteger thread=new AtomicInteger();

	public SimpleHashList() {
		this(4);
	}

	public SimpleHashList(int cap) {
		this.keys = new Object[IdentityHash.tableSizeFor(cap)];
	}

	public int put(@NotNull T key) {
		if (key == null) throw new NullPointerException();
		if (count + thread.incrementAndGet() >= keys.length) {//提前扩容，防止意外
			synchronized (this) {
				if (count + thread.get() >= keys.length)
					resize(count + thread.get());
			}
		}
		int i;
		Object[] array;
		do {
			array = this.keys;
			i = IdentityHash.findNullOrEqual(key, array);
		} while (array != keys);
		if (keys[i] == null) {
			synchronized (this) {
				if (keys[i] == null) {
					keys[i] = key;
					count++;
				} else {
					thread.decrementAndGet();
					return put(key);
				}
			}

		}
		thread.decrementAndGet();
		return i;
	}

	public T get(int index) {
		return (T) keys[index];
	}

	public int indexOf(T key) {
		if (key == null) return -1;
		int i = IdentityHash.findNullOrEqual(key, keys);
		return keys[i] == null ? -1 : i;
	}

	public boolean contains(@NotNull T key) {
		if (key == null) throw new NullPointerException();
		int i = IdentityHash.findNullOrEqual(key, keys);
		return keys[i] != null;
	}

	private void resize(int count) {
		Object[] newKeys = new Object[count * 2];
		IdentityHash.putAll(keys, newKeys);
		keys = newKeys;
		System.out.println("新容量："+keys.length);
	}

	public boolean remove(@NotNull T key) {
		if (key == null) return true;
		int i = IdentityHash.findNullOrEqual(key, keys);
		if (keys[i] != null) {
			keys[i] = null;
			count--;
			return true;
		}
		return false;
	}

	public int size() {
		return count;
	}

	@Override
	public String toString() {
		return Arrays.toString(keys);
	}
}
