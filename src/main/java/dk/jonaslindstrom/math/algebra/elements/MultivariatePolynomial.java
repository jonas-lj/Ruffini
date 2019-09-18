package dk.jonaslindstrom.math.algebra.elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.math.util.StringUtils;

public class MultivariatePolynomial<E> implements BiFunction<Vector<E>, Ring<E>, E> {

  private SortedMap<int[], E> terms;
  private int variables;
  
  public static TermComparator DEFAULT_ORDERING = new LexicographicalOrdering();

  public interface TermComparator extends Comparator<int[]> {

  }

  public static class LexicographicalOrdering implements TermComparator {

    @Override
    public int compare(int[] o1, int[] o2) {
      assert (o1.length == o2.length);
      for (int i = 0; i < o1.length; i++) {
        if (o1[i] < o2[i]) {
          return -1;
        } else if (o1[i] > o2[i]) {
          return 1;
        }
      }
      return 0;
    }

  }
  
  public static class GradedLexicographicalOrdering implements TermComparator {
    
    private LexicographicalOrdering lex;

    public GradedLexicographicalOrdering() {
      lex = new LexicographicalOrdering();
    }
    
    @Override
    public int compare(int[] o1, int[] o2) {
      assert (o1.length == o2.length);
      
      int sum1 = Arrays.stream(o1).sum();
      int sum2 = Arrays.stream(o2).sum();
      
      if (sum1 != sum2) {
        return Integer.compare(sum1, sum2);
      }
      
      return lex.compare(o1, o2);
    }
  }

  public static class Builder<S> {

    private final SortedMap<int[], S> terms;
    private Ring<S> ring;
    private int variables;

    public Builder(int variables, Ring<S> ring, TermComparator comparator) {
      this.variables = variables;
      this.ring = ring;
      terms = new TreeMap<>(comparator);
    }

    public Builder(int n, Ring<S> ring) {
      this(n, ring, DEFAULT_ORDERING);
    }

    public Builder<S> add(S c, int... degree) {
      assert(degree.length == variables);
      if (terms.containsKey(degree)) {
        terms.put(degree, ring.add(terms.get(degree), c));
      } else {
        terms.put(degree, c);
      }
      return this;
    }
    
    private void reduce() {
      terms.entrySet().removeIf(e -> ring.equals(e.getValue(), ring.getZero()));
     
      if (terms.isEmpty()) {
        add(ring.getZero(), new int[variables]);
      }
    }

    public MultivariatePolynomial<S> build() {
      reduce();
      return new MultivariatePolynomial<>(variables, terms);
    }
    
    public String toString() {
      return terms.toString();
    }

  }

  private MultivariatePolynomial(int variables, SortedMap<int[], E> terms) {
    this.variables = variables;
    this.terms = terms;
  }

  private String getVariable(int i) {
    if (variables < 4) {
      if (i == 0) {
        return "x";
      } else if (i == 1) {
        return "y";
      } else if (i == 2) {
        return "z";
      }
    } 
    return "x" + StringUtils.subscript(Integer.toString(i));
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int[] x : terms.keySet()) {
      E e = terms.get(x);
      sb.append(e.toString());
      for (int i = 0; i < x.length; i++) {
        if (x[i] > 0) {
          sb.append(getVariable(i));
          if (x[i] > 1) {
            sb.append(StringUtils.superscript(Integer.toString(x[i])));
          }
        }
      }
      if (x != terms.lastKey()) {
        sb.append(" + ");
      }
    }
    return sb.toString();
  }

  @Override
  public E apply(Vector<E> x, Ring<E> ring) {
    E result = ring.getZero();
    for (int[] d : terms.keySet()) {
      E c = terms.get(d);
      result = ring.add(result, ring.multiply(c, applyTerm(x, d, ring)));
    }
    return result;
  }
  
  public int variables() {
    return variables;
  }

  private static <S> S applyTerm(Vector<S> x, int[] degree, Ring<S> ring) {
    assert (x.getDimension() == degree.length);
    Power<S> power = new Power<>(ring);
    S result = ring.getIdentity();
    for (int i = 0; i < degree.length; i++) {
      result = ring.multiply(result, power.apply(x.get(i), degree[i]));
    }
    return result;
  }

  private static int[] multiply(int[] a, int[] b) {
    assert (a.length == b.length);
    int n = a.length;
    int[] c = new int[n];
    for (int i = 0; i < n; i++) {
      c[i] = a[i] + b[i];
    }
    return c;
  }

  public static <T> MultivariatePolynomial<T> multiply(MultivariatePolynomial<T> a,
      MultivariatePolynomial<T> b, Ring<T> ring) {
    assert(a.variables == b.variables);
    Builder<T> builder = new Builder<>(a.variables, ring);
    for (int[] ai : a.terms.keySet()) {
      for (int[] bi : b.terms.keySet()) {
        builder.add(ring.multiply(a.terms.get(ai), b.terms.get(bi)), multiply(ai, bi));
      }
    }
    return builder.build();
  }

  public E getCoefficient(int[] degree) {
    return terms.get(degree);
  }
  
  public int[] leadingMonomial() {
    return terms.lastKey();
  }
  
  public E leadingCoefficient() {
    return getCoefficient(terms.lastKey());
  }

  public Iterable<Pair<int[], E>> coefficients() {
    return () -> terms.keySet().stream().map(d -> new Pair<>(d, getCoefficient(d))).iterator();
  }

  public static <T> MultivariatePolynomial<T> add(MultivariatePolynomial<T> a,
      MultivariatePolynomial<T> b, Ring<T> ring) {
    assert(a.variables == b.variables);
    Builder<T> builder = new Builder<>(a.variables, ring);
    for (Pair<int[], T> ai : a.coefficients()) {
      builder.add(ai.second, ai.first);
    }

    for (Pair<int[], T> bi : b.coefficients()) {
      builder.add(bi.second, bi.first);
    }
    return builder.build();
  }
  
  public Iterable<int[]> monomials() {
    return Collections.unmodifiableSet(terms.keySet());
  }
  
  public static <T> MultivariatePolynomial<T> monomial(T coefficient, int ... degree) {
    SortedMap<int[], T> terms = new TreeMap<>(DEFAULT_ORDERING);
    terms.put(degree, coefficient);
    return new MultivariatePolynomial<T>(degree.length,terms);
  }

  public static <T> MultivariatePolynomial<T> constant(T coefficient, int variables) {
    return monomial(coefficient, new int[variables]);
  }
  
  public <X> MultivariatePolynomial<X> mapCoefficients(Function<E, X> converter) {
    SortedMap<int[], X> newMap = new TreeMap<>(terms.comparator());
    for (int[] term : terms.keySet()) {
      newMap.put(term, converter.apply(terms.get(term)));
    }
    return new MultivariatePolynomial<X>(variables, newMap);
  }

}
