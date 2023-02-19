package dk.jonaslindstrom.arithmeticparser;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MultiOperator<T> implements Function<List<T>, T> {

  private final int arguments;
  private final Function<List<T>, T> function;

  public MultiOperator(int arguments, Function<List<T>, T> function) {
    this.arguments = arguments;
    this.function = function;
  }
  
  public MultiOperator(Function<T, T> function) {
    this(1, l -> function.apply(l.get(0)));
  }
  
  public MultiOperator(BiFunction<T, T, T> function) {
    this(2, l -> function.apply(l.get(0), l.get(1)));
  }
  
  @Override
  public T apply(List<T> t) {
    assert(t.size() == arguments);
    return function.apply(t);
  }
  
  public int getArguments() {
    return arguments;
  }
}
