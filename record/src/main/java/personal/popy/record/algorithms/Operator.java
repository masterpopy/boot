package personal.popy.record.algorithms;
@SuppressWarnings("unused")
public abstract class Operator implements Comparable<Operator> {
	protected final int priority;//运算符优先度
	protected final String operator;//运算符
	protected final char lead;//前导字符


	public abstract double calculate(double first, double second);

	protected Operator(int priority, String operator) {
		this.priority = priority;
		this.operator = operator;
		this.lead = operator.charAt(0);
	}

	@Override
	public int compareTo(Operator o) {
		return o.priority - this.priority;
	}

	@Override
	public String toString() {
		return operator;
	}

	private final static class AddOperator extends Operator {

		public AddOperator() {
			super(10, "+");
		}

		@Override
		public double calculate(double first, double second) {
			return first + second;
		}
	}

	private final static class SubOperator extends Operator {

		public SubOperator() {
			super(10, "-");
		}

		@Override
		public double calculate(double first, double second) {
			return first - second;
		}
	}

	private final static class MulOperator extends Operator {

		public MulOperator() {
			super(20, "*");
		}

		@Override
		public double calculate(double first, double second) {
			return first * second;
		}
	}

	private final static class DivOperator extends Operator {
		public DivOperator() {
			super(20, "/");
		}

		@Override
		public double calculate(double first, double second) {
			return first / second;
		}
	}

	private final static class ModOperator extends Operator {
		public ModOperator() {
			super(20, "%");
		}

		@Override
		public double calculate(double first, double second) {
			return first % second;
		}
	}

}
