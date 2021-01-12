package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.EuclideanDomain;
import dk.jonaslindstrom.math.util.Pair;

public class Integers implements EuclideanDomain<Integer> {

  private static final Integers instance = new Integers();

  public static Integers getInstance() {
    return instance;
  }

  private Integers() {

  }

  @Override
  public Integer multiply(Integer a, Integer b) {
    return a * b;
  }

  @Override
  public Integer getIdentity() {
    return 1;
  }

  @Override
  public String toString(Integer a) {
    return Integer.toString(a);
  }

  @Override
  public Integer add(Integer a, Integer b) {
    return a + b;
  }

  @Override
  public Integer negate(Integer a) {
    return -a;
  }

  @Override
  public Integer getZero() {
    return 0;
  }

  @Override
  public boolean equals(Integer a, Integer b) {
    return a.equals(b);
  }

  @Override
  public Pair<Integer, Integer> divisionWithRemainder(Integer a, Integer b) {
    int residue = Math.floorMod(a, b);
    int quotient = (a - residue) / b;
    return new Pair<>(quotient, residue);
  }

  @Override
  public String toString() {
    return "ℤ";
  }

  @Override
  public Integer norm(Integer a) {
    return Math.abs(a);
  }

  @Override
  public int getCharacteristics() {
    return 0;
  }
}
