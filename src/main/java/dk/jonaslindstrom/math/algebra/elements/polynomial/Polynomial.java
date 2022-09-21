package dk.jonaslindstrom.math.algebra.elements.polynomial;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.concretisations.Integers;
import dk.jonaslindstrom.math.algebra.elements.vector.ConstructiveVector;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Polynomial<E> implements BiFunction<E, Ring<E>, E> {

  private final SortedMap<Integer, E> terms;

  private Polynomial(SortedMap<Integer, E> terms) {
    this.terms = Collections.unmodifiableSortedMap(terms);
  }

  private Polynomial(Map<Integer, E> terms) {
    this.terms = Collections.unmodifiableSortedMap(new ConcurrentSkipListMap<>(terms));
  }

  public Polynomial(int degree, IntFunction<E> populator) {
    this(IntStream.rangeClosed(0, degree).boxed().collect(Collectors.toList()), populator);
  }

  public Polynomial(Collection<Integer> nonZero, IntFunction<E> populator) {
    this(nonZero.stream().collect(Collectors.toMap(Integer::valueOf, populator::apply)));
  }

  public static <T> Polynomial<T> constant(T constant) {
    return Polynomial.monomial(constant, 0);
  }

  public static <T> Polynomial<T> monomial(T coefficient, int degree) {
    return new Polynomial<>(Collections.singletonMap(degree, coefficient));
  }

  /**
   * Construct a new polynomial with the given coefficients. The constant coefficient is first, the
   * linear is second etc.
   */
  @SafeVarargs
  public static <T> Polynomial<T> of(Ring<T> ring, T... coefficients) {
    Builder<T> p = new Builder<>(ring);
    for (int i = 0; i < coefficients.length; i++) {
      p.set(i, coefficients[i]);
    }
    return p.build();
  }

  public static Polynomial<Integer> of(Integer... coefficients) {
    return of(Integers.getInstance(), coefficients);
  }

  private static Polynomial<Integer> parsePolynomial(String string, String variable) {
    String[] terms = string.replaceAll("-", "+-").replace(" ", "").split("\\+");

    Builder<Integer> p = new Builder<>(Integers.getInstance());

    for (String term : terms) {
      String[] s = term.split(variable);

      int power = -1;
      int coefficient;

      if (s.length == 0) {
        // A term of just "x"
        power = 1;
        coefficient = 1;
      } else {
        if (s.length == 2) {
          power = Integer.parseInt(s[1].replaceAll("\\^", ""));
        } else if (s.length == 1) {
          if (term.contains(variable)) {
            power = 1;
          } else {
            power = 0;
          }
        }

        if (s[0].length() > 0) {
          coefficient = Integer.parseInt(s[0]);
        } else {
          coefficient = 1;
        }
      }
      p.set(power, coefficient);
    }

    return p.build();
  }

  public static Polynomial<Integer> parse(String polynomial, String variable) {
    return parsePolynomial(polynomial, variable);
  }

  public static Polynomial<Integer> parse(String polynomial) {
    return parsePolynomial(polynomial, "x");
  }

  public void forEach(BiConsumer<Integer, E> consumer) {
    for (Integer i : terms.keySet()) {
      consumer.accept(i, terms.get(i));
    }
  }

  public void forEachInParallel(BiConsumer<Integer, E> consumer) {
    terms.keySet().stream().parallel().forEach(i -> consumer.accept(i, terms.get(i)));
  }

  public <X> Polynomial<X> mapCoefficients(Function<E, X> converter) {
    Map<Integer, X> newTerms = terms.keySet().stream()
        .collect(Collectors.toMap(i -> i, i -> converter.apply(terms.get(i))));
    return new Polynomial<>(newTerms);
  }

  public Polynomial<E> removeTerms(Predicate<E> predicate) {
    Map<Integer, E> newTerms = terms.keySet().stream().filter(p -> !predicate.test(terms.get(p)))
        .collect(Collectors.toMap(i -> i, terms::get));
    return new Polynomial<>(newTerms);
  }

  public Polynomial<E> scale(E scale, Ring<E> ring) {
    return mapCoefficients(e -> ring.multiply(scale, e));
  }

  @Override
  public E apply(E input, Ring<E> ring) {
    Power<E> repeatedSquaring = new Power<>(ring);

    E result = ring.getZero();
    E variable = ring.getIdentity();
    int previous = 0;
    for (Integer current : terms.keySet()) {
      variable = ring.multiply(variable, repeatedSquaring.apply(input, current - previous));
      previous = current;
      result = ring.add(result, ring.multiply(terms.get(current), variable));
    }
    return result;
  }

  public int degree() {
    return terms.lastKey();
  }

  /**
   * Get the <i>i</i>'th coefficient or, if it is not present, <code>null</code>.
   */
  public E getCoefficient(int i) {
    return terms.get(i);
  }

  /**
   * Return the coefficients of this polynomial as a vector. The coefficients (of degree less than
   * the degree of the polynomial) that are not present will be replaced by the given zero value.
   */
  public Vector<E> vector(E zero) {
    return new ConstructiveVector<>(degree() + 1, i -> {
      E e = getCoefficient(i);
      if (e != null) {
        return e;
      } else {
        return zero;
      }
    });
  }

  @Override
  public String toString() {
    return toString("x");
  }

  public String toString(String variable, Function<E, String> stringRepresentation) {

    if (terms.size() == 0) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    for (Integer i : terms.keySet()) {
      boolean negative = false;
      if (!i.equals(terms.firstKey())) {

        if (stringRepresentation.apply(terms.get(i)).startsWith("-")) {
          sb.append(" - ");
          negative = true;
        } else {
          sb.append(" + ");
        }
      }

      String c = stringRepresentation.apply(terms.get(i));
      if (negative) {
        c = c.substring(1);
      }

      if (!c.equals("1") | i == 0) {
        sb.append(c);
      }

      if (i == 1) {
        sb.append(variable);
      } else if (i > 1) {
        sb.append(variable).append("^").append(i);
      }

    }
    return sb.toString();
  }

  public String toString(String variable) {
    return toString(variable, E::toString);
  }

  public static class Builder<S> {

    private final Ring<S> ring;
    private final SortedMap<Integer, S> terms = new TreeMap<>();

    public Builder(Ring<S> ring) {
      this.ring = ring;
    }

    /**
     * Set the <i>i</i>'th coefficient of the polynomial being built. If this coefficient has been
     * set before, it will be overwritten.
     */
    public Builder<S> set(int i, S a) {
      terms.put(i, a);
      return this;
    }

    /**
     * Add a value to the <i>i</i>'th coefficient of the polynomial being built.
     */
    public synchronized Builder<S> addTo(int i, S a) {
      if (terms.containsKey(i)) {
        set(i, ring.add(terms.get(i), a));
      } else {
        terms.put(i, a);
      }
      return this;
    }

    /**
     * Remove zero terms.
     */
    private Builder<S> reduce() {
      terms.entrySet().removeIf(e -> ring.equals(e.getValue(), ring.getZero()));
      if (terms.isEmpty()) {
        set(0, ring.getZero());
      }
      return this;
    }

    public Polynomial<S> build() {
      reduce();
      return new Polynomial<>(terms);
    }
  }

}
