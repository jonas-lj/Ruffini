package dk.jonaslindstrom.math.util;

public class Triple<E, F, G> {

  public E first;
  public F second;
  public G third;

  public Triple(E first, F second, G third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public E getFirst() {
    return first;
  }

  public F getSecond() {
    return second;
  }

  public G getThird() {
    return third;
  }

  @Override
  public String toString() {
    return "(" + first + ", " + second + ", " + third + ")";
  }


}
