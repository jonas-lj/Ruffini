package dk.jonaslindstrom.math.algebra.elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.math.util.StringUtils;

public class MultivariatePolynomial<E> implements BiFunction<Vector<E>, Ring<E>, E> {

  public static MonomialOrdering DEFAULT_ORDERING = new LexicographicalOrdering();

  private final SortedMap<Monomial, E> terms;
  private final int variables;

  private MultivariatePolynomial(int variables, SortedMap<Monomial, E> terms) {
    this.variables = variables;
    this.terms = terms;
  }

  public static class Monomial implements Iterable<Integer> {

    private final int[] degree;

    private Monomial(int[] degree) {
      this.degree = degree;
    }

    public static Monomial of(int ... n) {
      return new Monomial(n);
    }

    public int variables() {
      return degree.length;
    }

    public int degree(int i) {
      return degree[i];
    }

    public Monomial multiply(Monomial other) {
      assert (this.variables() == other.variables());
      int n = this.variables();
      int[] c = new int[n];
      for (int i = 0; i < n; i++) {
        c[i] = this.degree[i] + other.degree[i];
      }
      return Monomial.of(c);
    }

    public Monomial divideBy(Monomial other) {
      assert (this.variables() == other.variables());
      assert (other.divides(this));

      int k = this.variables();
      int[] result = new int[k];
      for (int i = 0; i < k; i++) {
        result[i] = this.degree[i] - other.degree[i];
      }
      return Monomial.of(result);
    }

    public boolean divides(Monomial other) {
      assert (this.variables() == other.variables());
      int k = this.variables();
      for (int i = 0; i < k; i++) {
        if (this.degree[i] > other.degree[i]) {
          return false;
        }
      }
      return true;
    }

    public Monomial lcm(Monomial other) {
      assert (this.variables() == other.variables());
      int n = this.variables();
      int[] c = new int[n];
      for (int i = 0; i < n; i++) {
        c[i] = Math.max(this.degree[i], other.degree[i]);
      }
      return Monomial.of(c);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(degree);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Monomial other = (Monomial) obj;
      return Arrays.equals(degree, other.degree);
    }

    @Override
    public Iterator<Integer> iterator() {
      return IntStream.of(degree).iterator();
    }

    private String getVariable(int i) {
      if (degree.length < 4) {
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

    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < degree.length; i++) {
        if (degree[i] > 0) {
          sb.append(getVariable(i));
          if (degree[i] > 1) {
            sb.append(StringUtils.superscript(Integer.toString(degree[i])));
          }
        }
      }
      return sb.toString();
    }

    private <S> S applyTerm(Vector<S> a, Ring<S> ring) {
      assert (a.getDimension() == degree.length);
      Power<S> power = new Power<>(ring);
      S result = ring.getIdentity();
      for (int i = 0; i < degree.length; i++) {
        result = ring.multiply(result, power.apply(a.get(i), degree[i]));
      }
      return result;
    }
  }

  public interface MonomialOrdering extends Comparator<Monomial> {

  }

  public static class LexicographicalOrdering implements MonomialOrdering {

    @Override
    public int compare(Monomial o1, Monomial o2) {
      assert (o1.variables() == o2.variables());
      int n = o1.variables();
      for (int i = 0; i < n; i++) {
        if (o1.degree(i) < o2.degree(i)) {
          return -1;
        } else if (o1.degree(i) > o2.degree(i)) {
          return 1;
        }
      }
      return 0;
    }

  }

  public static class GradedLexicographicalOrdering implements MonomialOrdering {

    private final LexicographicalOrdering lex;

    public GradedLexicographicalOrdering() {
      lex = new LexicographicalOrdering();
    }

    private int sum(int[] a) {
      int c = 0;
      for (int ai : a) {
        c += ai;
      }
      return c;
    }

    @Override
    public int compare(Monomial o1, Monomial o2) {
      assert (o1.variables() == o2.variables());

      int sum1 = sum(o1.degree);
      int sum2 = sum(o2.degree);

      if (sum1 != sum2) {
        return Integer.compare(sum1, sum2);
      }

      return lex.compare(o1, o2);
    }
  }

  public static class Builder<S> {

    private final SortedMap<Monomial, S> terms;
    private final Ring<S> ring;
    private final int variables;

    public Builder(int variables, Ring<S> ring) {
      this(variables, ring, DEFAULT_ORDERING);
    }

    public Builder(int variables, Ring<S> ring, MonomialOrdering ordering) {
      this.variables = variables;
      this.ring = ring;
      terms = new TreeMap<>(ordering);
    }

    public Builder<S> add(S c, Monomial degree) {
      assert(degree.variables() == variables);
      if (terms.containsKey(degree)) {
        terms.put(degree, ring.add(terms.get(degree), c));
      } else {
        terms.put(degree, c);
      }
      return this;
    }

    public Builder<S> add(S c, int... degree) {
      return add(c, Monomial.of(degree));
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    Iterator<Monomial> it = terms.keySet().iterator();
    while (it.hasNext()) {
      Monomial x = it.next();
      E e = terms.get(x);
      if (!e.toString().equals("1")) {
        sb.append(e.toString());
      }
      sb.append(x.toString());
      if (it.hasNext()) {
        sb.append(" + ");
      }
    }
    return sb.toString();
  }

  @Override
  public E apply(Vector<E> x, Ring<E> ring) {
    E result = ring.getZero();
    for (Monomial d : terms.keySet()) {
      E c = terms.get(d);
      result = ring.add(result, ring.multiply(c, d.applyTerm(x, ring)));
    }
    return result;
  }

  public int variables() {
    return variables;
  }

  public static <T> MultivariatePolynomial<T> multiply(MultivariatePolynomial<T> a,
      MultivariatePolynomial<T> b, Ring<T> ring) {
    assert(a.variables == b.variables);
    Builder<T> builder = new Builder<>(a.variables, ring);
    for (Monomial ai : a.terms.keySet()) {
      for (Monomial bi : b.terms.keySet()) {
        builder.add(ring.multiply(a.terms.get(ai), b.terms.get(bi)), ai.multiply(bi));
      }
    }
    return builder.build();
  }

  public E getCoefficient(int ... degree) {
    return terms.get(Monomial.of(degree));
  }

  public E getCoefficient(Monomial degree) {
    return terms.get(degree);
  }

  public Monomial leadingMonomial() {
    return terms.lastKey();
  }

  public Monomial leadingMonomial(Comparator<Monomial> ordering) {
    if (ordering.equals(terms.comparator())) {
      return terms.lastKey();
    }
    return Collections.max(terms.keySet(), ordering);
  }

  public E leadingCoefficient(Comparator<Monomial> ordering) {
    return getCoefficient(leadingMonomial(ordering));
  }

  public E leadingCoefficient() {
    return terms.get(terms.firstKey());
  }

  public Iterable<Pair<Monomial, E>> coefficients() {
    return () -> terms.keySet().stream().map(d -> new Pair<>(d, getCoefficient(d))).iterator();
  }

  public static <T> MultivariatePolynomial<T> add(MultivariatePolynomial<T> a,
      MultivariatePolynomial<T> b, Ring<T> ring) {
    assert(a.variables == b.variables);
    Builder<T> builder = new Builder<>(a.variables, ring);
    for (Pair<Monomial, T> ai : a.coefficients()) {
      builder.add(ai.second, ai.first);
    }

    for (Pair<Monomial, T> bi : b.coefficients()) {
      builder.add(bi.second, bi.first);
    }
    return builder.build();
  }

  public Iterable<Monomial> monomials() {
    return Collections.unmodifiableSet(terms.keySet());
  }

  public static <T> MultivariatePolynomial<T> monomial(T coefficient, int ... degree) {
    return monomial(coefficient, Monomial.of(degree));
  }

  public static <T> MultivariatePolynomial<T> monomial(T coefficient, Monomial degree) {
    SortedMap<Monomial, T> terms = new TreeMap<>(DEFAULT_ORDERING);
    terms.put(degree, coefficient);
    return new MultivariatePolynomial<T>(degree.variables(),terms);
  }

  public static <T> MultivariatePolynomial<T> constant(T coefficient, int variables) {
    return monomial(coefficient, new int[variables]);
  }

  public <F> MultivariatePolynomial<F> mapCoefficients(Function<E, F> converter) {
    SortedMap<Monomial, F> newTerms = new TreeMap<>(DEFAULT_ORDERING);
    for (Monomial degree : terms.keySet()) {
      newTerms.put(degree, converter.apply(terms.get(degree)));
    }
    return new MultivariatePolynomial<F>(variables, newTerms);
  }

}
