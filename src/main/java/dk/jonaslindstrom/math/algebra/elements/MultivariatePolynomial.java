package dk.jonaslindstrom.math.algebra.elements;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.StringUtils;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;

public class MultivariatePolynomial<E> implements BiFunction<Vector<E>, Ring<E>, E> {

  private SortedSet<Term<E>> terms;
  //
  //
  //
  // /**
  // * This class represents an indeterminate in a term in a polynomial. The varible parameter is an
  // * identifier for the the indeterminate to a specific power.
  // */
  // public static class Indeterminate implements Comparable<Indeterminate> {
  // public final int indeterminate, degree;
  //
  // private Indeterminate(int indeterminate, int degree) {
  // this.indeterminate = indeterminate;
  // this.degree = degree;
  // }
  //
  // public static Indeterminate of(int var, int pow) {
  // return new Indeterminate(var, pow);
  // }
  //
  // @Override
  // public int compareTo(Indeterminate o) {
  // if (Integer.compare(indeterminate, o.indeterminate) != 0) {
  // // 0'th indeterminate has the highest weight
  // return -Integer.compare(indeterminate, o.indeterminate);
  // }
  // return Integer.compare(degree, o.degree);
  // }
  //
  // @Override
  // public String toString() {
  // String s = "x" + StringUtils.subscript(Integer.toString(indeterminate));
  // if (degree > 1) {
  // s += StringUtils.superscript(Integer.toString(degree));
  // }
  // return s;
  // }
  // }

  public static class Term<S> implements Comparable<Term<S>>, BiFunction<Vector<S>, Ring<S>, S> {
    public final S coefficient;
    public final int[] degree;

    private Term(S coefficient, int... degree) {
      this.degree = degree;
      this.coefficient = coefficient;
    }

    public static <T> Term<T> of(T coefficient, int... degree) {
      return new Term<>(coefficient, degree);
    }

    private static int[] combine(int[] a, int[] b) {
      assert (a.length == b.length);
      int n = a.length;
      int[] c = new int[n];

      for (int i = 0; i < n; i++) {
        c[i] = a[i] + b[i];
      }
      return c;
    }

    public static <T> Term<T> multiply(Term<T> a, Term<T> b, Ring<T> ring) {
      return new Term<>(ring.multiply(a.coefficient, b.coefficient), combine(a.degree, b.degree));
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(coefficient.toString());
      for (int i = 0; i < degree.length; i++) {
        sb.append("x");
        sb.append(StringUtils.subscript(Integer.toString(i)));
        sb.append(StringUtils.superscript(Integer.toString(degree[i])));
      }
      return sb.toString();
    }

    @Override
    public int compareTo(Term<S> other) {
      return compare(degree, other.degree);
    }

    private static int compare(int[] a, int[] b) {
      assert (a.length == b.length);
      int n  =a.length;
      for (int i = 0; i < n; i++) {
        int c = Integer.compare(a[i], b[i]);
        if (c != 0) {
          return c;
        }
      }
      return 0;
    }

    @Override
    public S apply(Vector<S> v, Ring<S> ring) {
      S c = coefficient;
      Power<S> power = new Power<>(ring);
      
      for (int i = 0; i < degree.length; i++) {
        if (degree[i] != 0) {
          c = ring.multiply(c, power.apply(v.get(i), degree[i]));
        }
      }
      return c;
    }
  }

  public static class PolynomialBuilder<S> {

    private final SortedSet<Term<S>> terms = new TreeSet<>();

    private void add(Term<S> term) {
      terms.add(term);
    }

    public void add(S c, int... degree) {
      terms.add(Term.of(c, degree));
    }

    public MultivariatePolynomial<S> build() {
      return new MultivariatePolynomial<>(terms);
    }

  }

  public MultivariatePolynomial(SortedSet<Term<E>> terms) {
    this.terms = terms;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator<Term<E>> it = terms.iterator();

    while (it.hasNext()) {
      Term<E> term = it.next();
      sb.append(term.toString());
      if (it.hasNext()) {
        sb.append(" + ");
      }
    }
    return sb.toString();
  }

  @Override
  public E apply(Vector<E> x, Ring<E> ring) {
    E c = ring.getZero();
    for (Term<E> term : terms) {
      c = ring.add(c, term.apply(x, ring));
    }
    return c;
  }

  public static <T> MultivariatePolynomial<T> multiply(MultivariatePolynomial<T> a,
      MultivariatePolynomial<T> b, Ring<T> ring) {
    PolynomialBuilder<T> builder = new PolynomialBuilder<>();
    for (Term<T> ai : a.terms) {
      for (Term<T> bi : b.terms) {
        builder.add(Term.multiply(ai, bi, ring));
      }
    }
    return builder.build();
  }
}
