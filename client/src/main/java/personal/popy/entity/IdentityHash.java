package personal.popy.entity;

import java.util.Comparator;

public final class IdentityHash {

	public static int findNullEntry(Object key, Object[] array) {
		//noinspection ComparatorMethodParameterNotUsed
		return find(key, array, (k, item) -> item != null ? 1 : 0);
	}

	public static int find(Object key, Object[] array, Comparator<Object> finder) {
		int i = hash(key, array.length);
		int hash = i;
		while (finder.compare(key, array[i]) > 0) {
			i = nextKeyIndex(i, array.length);
			if (i == hash) throw new IllegalArgumentException("array should not be full");
		}
		return i;
	}

	public static void putAll(Object[] source, Object[] target) {
		for (Object key : source) {
			if (key == null) continue;
			target[findNullEntry(key, target)] = key;
		}
	}

	public static int tableSizeFor(int cap) {
		int MAXIMUM_CAPACITY = 1 << 30;
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}

	static int findNullOrEqual(Object key, Object[] array) {
		return find(key, array, (k, v) -> v != null && !v.equals(k) ? 1 : 0);
	}

	static int hash(Object key, int length) {
		return key.hashCode() & (length - 1);
	}

	static int nextKeyIndex(int index, int length) {
		return index + 1 < length ? index + 1 : 0;
	}
}
