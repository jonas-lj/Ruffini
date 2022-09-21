package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Monoid;
import dk.jonaslindstrom.math.algebra.elements.word.Word;

public class FreeMonoid implements Monoid<Word> {

  @Override
  public Word getIdentity() {
    return new Word();
  }

  @Override
  public Word multiply(Word a, Word b) {
    return a.multiply(b);
  }

  @Override
  public String toString(Word a) {
    return a.toString();
  }

  @Override
  public boolean equals(Word a, Word b) {
    return a.equals(b);
  }
}
