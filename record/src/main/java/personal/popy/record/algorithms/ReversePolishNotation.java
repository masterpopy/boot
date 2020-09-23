package personal.popy.record.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 中缀转后缀表达式：
 * 操作数：
 * 0.操作数直接加入操作数(输出)栈
 * 操作符：
 * 　  1.若栈空,若为左括号,若当前栈顶元素是(: 直接入操作符栈
 * <p>
 * 　　2.若是)，以此出栈，加入到输出栈，直至当前栈顶为(, 将(直接出栈
 * <p>
 * 　　3.如果当前栈不为空并且不是左括号, 如果栈顶存在优先度 大于 当前元素的操作符,依次弹出（直到空或遇到左括号）, 最后再将当前操作符加入操作符栈
 * 计算：
 * 　　4.最后将所有操作符栈加入输出栈，计算输出栈时从左往右计算
 */
@SuppressWarnings("unused")
public class ReversePolishNotation {

	private static final List<Operator> OPERATORS;

	static {
		Class<?>[] clz = Operator.class.getDeclaredClasses();
		OPERATORS = Stream.of(clz).map(ReversePolishNotation::newOperator).sorted().collect(Collectors.toList());
	}

	private static final Mark LEFT_PARENTHESE = new Mark("(");

	//操作符栈
	private final Stack<Object> operatorStack = new Stack<>();

	//结果集
	private final ArrayList<Object> out = new ArrayList<>();

	public double calc(String src) {
		int start = 0, length = src.length();
		while (start < length) {
			char ch = src.charAt(start);
			if (Character.isWhitespace(ch)) {
				start++;
			} else if (Character.isDigit(ch)) {
				int end = start + 1;
				while (end < length) {
					ch = src.charAt(end);
					if (!Character.isDigit(ch) && ch != '.') {
						break;
					}
					end++;
				}
				//数字直接输出
				out.add(Double.parseDouble(src.substring(start, end)));
				start = end;
			} else if (ch == ')') {
				Operator element;
				//弹出直到遇见左括号
				while ((element = (Operator) operatorStack.pop()) != LEFT_PARENTHESE) {
					out.add(element);
				}
				start++;
			} else if (ch == '(') {
				//直接入栈
				operatorStack.add(LEFT_PARENTHESE);
				start++;
			} else {
				Operator element = findOperator(ch);
				start += element.operator.length();
				Operator peek;
				while (!operatorStack.isEmpty() && (peek = (Operator) operatorStack.peek()) != LEFT_PARENTHESE
						&& element.priority <= peek.priority) {
					//将所有优先度大于当前元素的操作符弹出
					//相当于：如果栈顶元素不为空并且不是左括号，那么判断并将所有优先度比当前元素高的元素加入输出栈
					out.add(operatorStack.pop());
				}
				//最后再将当前元素加入操作符栈
				operatorStack.add(element);
			}
		}
		//将剩余操作符加入输出栈
		while (!operatorStack.isEmpty()) {
			out.add(operatorStack.pop());
		}
		//从左往右计算数据
		Stack<Object> calObj =  operatorStack;
		Stack<Object> calresult = operatorStack;
		for (int i = 0, size = out.size(); i < size; i++) {
			Object o = out.get(i);
			if (o instanceof Double) {
				calObj.push(o);
			} else {
				Double second = (Double) calresult.pop();
				Double first = (Double) calresult.pop();
				calresult.push(((Operator) o).calculate(first, second));
			}
		}
		out.clear();
		return (double) calresult.pop();
	}

	private static Operator findOperator(char lead) {
		for (int i = 0, l = OPERATORS.size(); i < l; i++) {
			Operator element = OPERATORS.get(i);
			if (element.lead == lead) {
				return element;
			}
		}
		throw new NumberFormatException();
	}

	private static Operator newOperator(Class<?> c) {
		try {
			return (Operator) c.newInstance();
		} catch (Exception e) {
			throw new InternalError();
		}
	}

	private static class Mark extends Operator {
		private Mark(String op) {
			super(0, op);
		}

		@Override
		public double calculate(double first, double second) {
			return 0;
		}
	}

}





