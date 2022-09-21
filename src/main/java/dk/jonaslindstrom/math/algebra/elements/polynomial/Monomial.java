package dk.jonaslindstrom.math.algebra.elements.polynomial;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Monomial implements Iterable<Integer> {

  final int[] degree;

  private Monomial(int[] degree) {
    this.degree = degree;
  }

  public static Monomial of(int... n) {
    return new Monomial(n);
  }

  public int variables() {
    return degree.length;
  }

  public int degree(int i) {
    return degree[i];
  }

  public int degree() {
    return Arrays.stream(degree).sum();
  }

  Monomial pad(int variables) {
    assert (variables >= degree.length);
    int[] newDegree = new int[variables];
    System.arraycopy(degree, 0, newDegree, 0, degree.length);
    return new Monomial(newDegree);
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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
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
    return "x_" + (i + 1);
  }

  public String toString() {

    StringBuilder sb = new StringBuilder();
    int[] nonZero = IntStream.range(0, degree.length).filter(i -> degree[i] > 0).toArray();

    for (int i = 0; i < nonZero.length; i++) {
      int termIndex = nonZero[i];
      sb.append(getVariable(termIndex));
      if (degree[termIndex] > 1) {
        sb.append("^").append(degree[termIndex]);
      }
      if (i < nonZero.length - 1) {
        sb.append(" ");
      }
    }

    return sb.toString();
  }

  public <S> S applyTerm(Vector<S> a, Ring<S> ring) {
    assert (a.size() == degree.length);
    Power<S> power = new Power<>(ring);
    S result = ring.getIdentity();
    for (int i = 0; i < degree.length; i++) {
      result = ring.multiply(result, power.apply(a.get(i), degree[i]));
    }
    return result;
  }
}
