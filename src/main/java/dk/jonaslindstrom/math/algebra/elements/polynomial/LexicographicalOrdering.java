package dk.jonaslindstrom.math.algebra.elements.polynomial;

public class LexicographicalOrdering implements MonomialOrdering {

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
