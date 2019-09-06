package dk.jonaslindstrom.math.algebra.elements;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.concretisations.Integers;
import dk.jonaslindstrom.math.algebra.elements.vector.ConstructiveVector;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.StringUtils;

public final class Polynomial<E> implements BiFunction<E, Ring<E>, E> {

  private final SortedMap<Integer, E> terms;

  public static class Builder<S> {

    private Ring<S> ring;

    public Builder(Ring<S> ring) {
      this.ring = ring;     
    }
    
    private final SortedMap<Integer, S> terms = new ConcurrentSkipListMap<>();

    /**
     * Set the <i>i</i>'th coefficient of the polynomial being built. If this coefficient has been set
     * before, it will be overwritten.
     * 
     * @param i
     * @param a
     */
    public void set(int i, S a) {
      terms.put(i, a);
    }
    
    /**
     * Add a value to the <i>i</i>'th coefficient of the polynomial being built.
     * 
     * @param i
     * @param a
     * @param ring
     */
    public void addTo(int i, S a) {
      if (terms.containsKey(i)) {
        set(i, ring.add(terms.get(i), a));
      } else {
        terms.put(i, a);
      }
    }

    /**
     * Remove zero terms.
     * 
     * @param ring
     */
    private void reduce() {
      for (Integer i : terms.keySet()) {
        if (ring.equals(terms.get(i), ring.getZero())) {
          terms.remove(i);
        }
      }
      if (terms.isEmpty()) {
        set(0, ring.getZero());
      }
    }

    public Polynomial<S> build() {
      reduce();
      return new Polynomial<>(terms);
    }
  }

  private Polynomial(SortedMap<Integer, E> terms) {
    this.terms = Collections.unmodifiableSortedMap(terms);
  }

  private Polynomial(Map<Integer, E> terms) {
    this.terms = Collections.unmodifiableSortedMap(new ConcurrentSkipListMap<>(terms));
  }
  
  public static <T> Polynomial<T> constant(T constant) {
    return Polynomial.monomial(constant, 0);
  }

  public static <T> Polynomial<T> monomial(T coefficient, int degree) {
    return new Polynomial<T>(Collections.singletonMap(degree, coefficient));
  }

  /**
   * Construct a new polynomial with the given coefficients. The constant coefficient is first, the
   * linear is second etc.
   * 
   * @param ring
   * @param c
   */
  @SafeVarargs
  public static <T> Polynomial<T> of(Ring<T> ring, T... coefficients) {
    Builder<T> p = new Builder<>(ring);
    for (int i = 0; i < coefficients.length; i++) {
      p.set(i, coefficients[i]);
    }
    return p.build();
  }

  private static Polynomial<Integer> parsePolynomial(String string, String variable) {
    String[] terms = string.replaceAll("\\-", "+-").replace(" ", "").split("\\+");

    Builder<Integer> p = new Builder<>(Integers.getInstance());

    for (String term : terms) {
      String[] s = term.split(variable);

      int power = -1;
      Integer coefficient = null;

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
  
  public void forEach(BiConsumer<Integer, E> consumer) {
    for (Integer i : terms.keySet()) {
      consumer.accept(i, terms.get(i));
    }
  }

  public static Polynomial<Integer> parse(String polynomial, String variable) {
    return parsePolynomial(polynomial, variable);
  }

  public <X> Polynomial<X> mapCoefficients(Function<E, X> converter) {
    Map<Integer, X> newTerms = terms.keySet().stream()
        .collect(Collectors.toMap(i -> i, i -> converter.apply(terms.get(i))));
    return new Polynomial<>(newTerms);
  }

  public Polynomial<E> scale(E scale, Ring<E> ring) {
    return mapCoefficients(e -> ring.multiply(scale, e));
  }

  @Override
  public E apply(E input, Ring<E> ring) {
    E result = ring.getZero();
    E variable = ring.getIdentity();

    int power = 0;
    for (Integer i : terms.keySet()) {
      while (power < i) {
        // TODO: Use repeated squaring?
        variable = ring.multiply(variable, input);
        power++;
      }
      result = ring.add(result, ring.multiply(terms.get(i), variable));
    }
    return result;
  }

  public <F> F apply(F input, Ring<F> vectors, BiFunction<E, F, F> scaling) {
    F result = vectors.getZero();
    F variable = vectors.getIdentity();
    int power = 0;
    for (Integer i : terms.keySet()) {
      while (power < i) {
        // TODO: Use repeated squaring?
        variable = vectors.multiply(variable, input);
        power++;
      }
      result = vectors.add(result, scaling.apply(terms.get(i), variable));
      variable = vectors.multiply(variable, input);
    }
    return result;
  }

  public int degree() {
    return terms.lastKey();
  }

  public E getCoefficient(int i) {
    return terms.get(i);
  }

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

  public String toString(Ring<E> ring) {
    return toString(e -> ring.equals(e, ring.getIdentity()), "x");
  }
  
  public String toString(String variable) {
    return toString(e -> false, variable);
  }

  @Override
  public String toString() {
    return toString("x");
  }
  
  public String toString(Predicate<E> isIdentity, String variable) {

    if (terms.size() == 0) {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    for (Integer i : terms.keySet()) {
      if (!isIdentity.test(terms.get(i))) {
        sb.append(terms.get(i).toString());
      }
      
      if (i == 1) {
        sb.append(variable);
      } else if (i > 1) {
        sb.append(variable + StringUtils.superscript(Integer.toString(i)));
      }
      
      if (i != terms.lastKey()) {
        sb.append(" + ");
      }      
    }
    return sb.toString();
  }

}
