package personal.popy.datastructure;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Collector<T, A, R>
 *     T：T是流中要收集的对象的泛型，就supplier生成的类型不断通过accumulator来迭代
 *     A：A是累加器的类型，累加器是在收集过程中用于累积部分结果的对象。
 *     R：R是收集操作得到的对象(通常但不一定是集合)的类型。最后用finisher来执行转换，如果中间类型与返回类型一致就不用换
		 首先supplier()会提供结果容器，然后accumulator()向结果容器中累积元素，最后finisher()将结果容器转换成最终的返回结果。
		 如果结果容器类型和最终返回结果类型一致，那么finisher()就可以不用执行，这就是为什么开头说“这是一个可选的操作”的原因。

		 而combiner()是和并行流相关的，在串行流中combiner()并不起作用

 *
 */
public class MyCollector<T, K, V> implements Collector<T, Map<K, V>, Map<K, V>> {

	Supplier<? extends K> keyMapper;
	Function<? super T, ? extends V> valueMapper;

	public static final Set<Collector.Characteristics> CH_ID
			= Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

	public MyCollector(Supplier<? extends K> keyMapper,
	                   Function<? super T, ? extends V> valueMapper) {
		this.keyMapper = keyMapper;
		this.valueMapper = valueMapper;
	}

	@Override
	public Supplier<Map<K, V>> supplier() {
		return HashMap::new;
	}

	@Override
	public BiConsumer<Map<K, V>, T> accumulator() {
		return (map, v) -> {
			map.put(keyMapper.get(), valueMapper.apply(v));
		};
	}

	@Override
	public BinaryOperator<Map<K, V>> combiner() {
		return (l, r) -> null;
	}

	@Override
	public Function<Map<K, V>, Map<K,V>> finisher() {
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		return CH_ID;
	}
}
