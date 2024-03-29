package dk.jonaslindstrom.arithmeticparser;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;

import java.text.ParseException;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * This class can evaluate expressions parsed to reverse polish notation using the {@link Parser}
 * class.
 *
 * @param <NumberT>
 */
public class Evaluator<NumberT> {

    private final Map<String, BinaryOperator<NumberT>> operators;
    private final Map<String, MultiOperator<NumberT>> functions;
    private final NumberParser<NumberT> parser;

    public Evaluator(Map<String, MultiOperator<NumberT>> functions,
                     Map<String, BinaryOperator<NumberT>> operators, NumberParser<NumberT> parser) {
        this.functions = functions;
        this.operators = operators;
        this.parser = parser;
    }

    public static Evaluator<Double> getDefault() {
        return getDefault(Collections.emptyMap());
    }

    public static Evaluator<Double> getDefault(Map<String, MultiOperator<Double>> functions) {
        return new Evaluator<Double>(functions, new HashMap<String, BinaryOperator<Double>>() {
            private static final long serialVersionUID = 1797227866125541488L;

            {
                put("+", (x, y) -> x + y);
                put("-", (x, y) -> x - y);
                put("*", (x, y) -> x * y);
                put("/", (x, y) -> x / y);
                put("^", (x, y) -> Math.pow(x, y));
            }
        }, Double::parseDouble);
    }

    public static <E> Evaluator<E> getField(Field<E> field, Function<String, E> parser, Map<String, MultiOperator<E>> functions) {
        return new Evaluator<E>(functions, new HashMap<>() {
            {
                put("+", field::add);
                put("-", field::subtract);
                put("*", field::multiply);
                put("/", field::divide);
            }
        }, parser::apply);
    }

    public static <E> Evaluator<E> getRing(Ring<E> field, Function<String, E> parser, Map<String, MultiOperator<E>> functions) {
        return new Evaluator<E>(functions, new HashMap<>() {
            {
                put("+", field::add);
                put("-", field::subtract);
                put("*", field::multiply);
            }
        }, parser::apply);
    }

    public static Double evaluate(String expression, Map<String, Double> variables,
                                  Map<String, MultiOperator<Double>> functions) {
        try {
            Parser<Double> parser = Parser.getDefault();

            List<Token> parsed = parser.parse(expression, new ArrayList<>(variables.keySet()),
                    new ArrayList<>(functions.keySet()));

            Evaluator<Double> eval = Evaluator.getDefault(functions);

            return eval.evaluate(parsed, variables);
        } catch (ParseException | EvaluationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double evaluate(String expression) {
        return evaluate(expression, Collections.emptyMap(), Collections.emptyMap());
    }

    /**
     * Given an expression as a list of tokens in RPN (reverse Polish Notation), this evaluator
     * computes the result.
     *
     * @param expression
     * @return
     * @throws EvaluationException
     */
    public NumberT evaluate(List<Token> expression, Map<String, NumberT> variables)
            throws EvaluationException {

        Deque<NumberT> stack = new LinkedList<>();

        for (Token token : expression) {
            switch (token.getType()) {
                case FUNCTION -> {
                    if (!functions.containsKey(token.getRepresentation())) {
                        throw new EvaluationException("Unkown function: " + token);
                    }
                    MultiOperator<NumberT> function = functions.get(token.getRepresentation());
                    if (stack.size() < function.getArguments()) {
                        throw new EvaluationException(
                                "Invalid expression - not enough inputs for function: " + token);
                    }
                    LinkedList<NumberT> inputs = new LinkedList<>();
                    for (int i = 0; i < function.getArguments(); i++) {
                        inputs.addFirst(stack.pop());
                    }
                    NumberT output = function.apply(inputs);
                    stack.push(output);
                }
                case LEFT_PARANTHESIS -> throw new EvaluationException("Expression should not contain paranthesis");
                case NUMBER -> {
                    NumberT value = parser.parse(token.getRepresentation());
                    stack.push(value);
                }
                case OPERATOR -> {
                    if (stack.size() < 2) {
                        throw new EvaluationException(
                                "Invalid expression - not enough inputs for operator: " + token);
                    }
                    NumberT b = stack.pop();
                    NumberT a = stack.pop();
                    if (!operators.containsKey(token.getRepresentation())) {
                        throw new EvaluationException("Unkown operator: " + token);
                    }
                    BinaryOperator<NumberT> operator = operators.get(token.getRepresentation());
                    NumberT c = operator.apply(a, b);
                    stack.push(c);
                }
                case VARIABLE -> {
                    if (!variables.containsKey(token.getRepresentation())) {
                        throw new EvaluationException("No such variable: " + token);
                    }
                    NumberT variableValue = variables.get(token.getRepresentation());
                    stack.push(variableValue);
                }
                default -> throw new EvaluationException("Unknown token: " + token);
            }

        }

        if (stack.size() != 1) {
            throw new EvaluationException(
                    "Invalid expression. Expected exactly one output but had " + stack);
        }
        return stack.pop();
    }
}
