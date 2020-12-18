package org.xblackcat.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 18: Operation Order ---
 * As you look out the window and notice a heavily-forested continent slowly appear over the horizon, you are interrupted by the child sitting next to you. They're curious if you could help them with their math homework.
 * <p>
 * Unfortunately, it seems like this "math" follows different rules than you remember.
 * <p>
 * The homework (your puzzle input) consists of a series of expressions that consist of addition (+), multiplication (*), and parentheses ((...)). Just like normal math, parentheses indicate that the expression inside must be evaluated before it can be used by the surrounding expression. Addition still finds the sum of the numbers on both sides of the operator, and multiplication still finds the product.
 * <p>
 * However, the rules of operator precedence have changed. Rather than evaluating multiplication before addition, the operators have the same precedence, and are evaluated left-to-right regardless of the order in which they appear.
 * <p>
 * For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are as follows:
 * <pre>
 * 1 + 2 * 3 + 4 * 5 + 6
 *   3   * 3 + 4 * 5 + 6
 *       9   + 4 * 5 + 6
 *          13   * 5 + 6
 *              65   + 6
 *                  71</pre>
 * Parentheses can override this order; for example, here is what happens if parentheses are added to form 1 + (2 * 3) + (4 * (5 + 6)):
 * <pre>
 * 1 + (2 * 3) + (4 * (5 + 6))
 * 1 +    6    + (4 * (5 + 6))
 *      7      + (4 * (5 + 6))
 *      7      + (4 *   11   )
 *      7      +     44
 *             51</pre>
 * Here are a few more examples:
 * <ul>
 * <li>2 * 3 + (4 * 5) becomes 26.</li>
 * <li>5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 437.</li>
 * <li>5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 12240.</li>
 * <li>((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 13632.</li>
 * </ul>
 * Before you can help with the homework, you need to understand it yourself. Evaluate the expression on each line of the homework; what is the sum of the resulting values?
 * <p>
 * --- Part Two ---
 * You manage to answer the child's questions and they finish part 1 of their homework, but get stuck when they reach the next section: advanced math.
 * <p>
 * Now, addition and multiplication have different precedence levels, but they're not the ones you're familiar with. Instead, addition is evaluated before multiplication.
 * <p>
 * For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are now as follows:
 * <pre>
 * 1 + 2 * 3 + 4 * 5 + 6
 *   3   * 3 + 4 * 5 + 6
 *   3   *   7   * 5 + 6
 *   3   *   7   *  11
 *      21       *  11
 *          231</pre>
 * Here are the other examples from above:
 *
 * <ul>
 * <li>1 + (2 * 3) + (4 * (5 + 6)) still becomes 51.</li>
 * <li>2 * 3 + (4 * 5) becomes 46.</li>
 * <li>5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 1445.</li>
 * <li>5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 669060.</li>
 * <li>((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 23340.</li>
 * </ul>What do you get if you add up the results of evaluating the homework problems using these new rules?
 */
public class Day18 {
    private static final Pattern LITERAL_PATTERN = Pattern.compile("\\d+|[()+*]");

    public static void main(String[] args) throws IOException {
//        part1();
        part2();
    }

    private static void part1() throws IOException {
        calculate("1 + 2 * 3 + 4 * 5 + 6", Day18::simpleOp);
        calculate("1 + (2 * 3) + (4 * (5 + 6))", Day18::simpleOp);
        calculate("2 * 3 + (4 * 5)", Day18::simpleOp);
        calculate("5 + (8 * 3 + 9 + 3 * 4 * 3)", Day18::simpleOp);
        calculate("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) ", Day18::simpleOp);
        calculate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", Day18::simpleOp);


        long sum;
        try (
                final InputStream stream = Day17.class.getResourceAsStream("/year2020/day18.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            sum = reader.lines().mapToLong(l -> calculate(susanin(l, Day18::simpleOp))).sum();
        }

        System.out.println("Homework sum is " + sum);
    }

    private static void part2() throws IOException {
        calculate("1 + 2 * 3 + 4 * 5 + 6", Day18::complexOp);
        calculate("1 + (2 * 3) + (4 * (5 + 6))", Day18::complexOp);
        calculate("2 * 3 + (4 * 5)", Day18::complexOp);
        calculate("5 + (8 * 3 + 9 + 3 * 4 * 3)", Day18::complexOp);
        calculate("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) ", Day18::complexOp);
        calculate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", Day18::complexOp);


        long sum;
        try (
                final InputStream stream = Day17.class.getResourceAsStream("/year2020/day18.txt");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            sum = reader.lines().mapToLong(l -> calculate(susanin(l, Day18::complexOp))).sum();
        }

        System.out.println("Homework sum is " + sum);
    }

    private static long calculate(String s, Function<String, Operation> operationParser) {
        final Deque<Unit> units = susanin(s, operationParser);
        units.forEach(System.out::println);
        final long result = calculate(units);
        System.out.println(s + " = " + result);
        return result;
    }

    private static long calculate(Queue<Unit> polishNotationExpression) {
        Deque<Value> stack = new ArrayDeque<>();

        for (Unit u : polishNotationExpression) {
            if (u instanceof Value v) {
                stack.push(v);
            } else if (u instanceof Operation o) {
                Value b = stack.pop();
                Value a = stack.pop();
                stack.push(o.apply(a, b));
            } else {
                throw new IllegalArgumentException("Invalid literal: " + u);
            }
        }

        return stack.pop().value;
    }

    /**
     * Parse text and represent expression in polish notation
     *
     * @param text            input text
     * @param operationParser helper function to set proper priority to an operations
     * @return operations and operands in polish notation
     */
    private static Deque<Unit> susanin(String text, Function<String, Operation> operationParser) {
        final Matcher matcher = LITERAL_PATTERN.matcher(text);
        Deque<Unit> result = new ArrayDeque<>();
        Deque<OperationInfo> opStack = new ArrayDeque<>();
        int level = 0;
        while (matcher.find()) {
            String token = matcher.group();

            Value value = Value.of(token);
            if (value != null) {
                result.add(value);
                continue;
            }

            if ("(".equals(token)) {
                level++;
            } else if (")".equals(token)) {
                OperationInfo op;
                while ((op = opStack.peek()) != null && op.level == level) {
                    opStack.poll();
                    result.add(op.op);
                }
                level--;
            } else {
                final Operation currentOp = operationParser.apply(token);
                final var peek = opStack.peek();
                if (peek != null && peek.level == level && peek.op.priority >= currentOp.priority) {
                    opStack.poll();
                    result.add(peek.op);
                }
                opStack.push(new OperationInfo(currentOp, level));
            }
        }

        OperationInfo op;
        while ((op = opStack.poll()) != null) {
            result.add(op.op);
        }

        return result;
    }

    private static Operation simpleOp(String token) {
        return new Operation(token.charAt(0), 0);
    }

    private static Operation complexOp(String token) {
        final char op = token.charAt(0);
        return new Operation(op, op == '+' ? 1 : 0);
    }

    private interface Unit {
    }

    private record Value(long value) implements Unit {
        static Value of(String str) {
            try {
                return new Value(Long.parseLong(str));
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private record Operation(char operand, int priority) implements Unit {
        Value apply(Value left, Value right) {
            return switch (operand) {
                case '+' -> new Value(left.value + right.value);
                case '*' -> new Value(left.value * right.value);
                default -> throw new IllegalArgumentException("Invalid operand: " + operand);
            };
        }
    }

    private record OperationInfo(Operation op, int level) {
    }
}
