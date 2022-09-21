package dk.jonaslindstrom.math.algebra.elements.polynomial;

public class GradedLexicographicalOrdering implements MonomialOrdering {

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
