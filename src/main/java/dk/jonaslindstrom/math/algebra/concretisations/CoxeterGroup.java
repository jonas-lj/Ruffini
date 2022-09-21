package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.word.Word;

public class CoxeterGroup extends FreeMonoid {

  private final Matrix<Integer> matrix;

  public CoxeterGroup(Matrix<Integer> matrix) {
    this.matrix = matrix;

    if (!matrix.isSquare()) {
      throw new IllegalArgumentException("Invalid Coxeter system");
    }

    for (int i = 0; i < matrix.getHeight(); i++) {
      for (int j = 0; j < matrix.getWidth(); j++) {
        if (i == j) {
          if (matrix.get(i,j) != 1) {
            throw new IllegalArgumentException("Invalid Coxeter system");
          }
        } else {
          if (!matrix.get(i, j).equals(matrix.get(j, i))) {
            throw new IllegalArgumentException("Invalid Coxeter system");
          }

          // We represent infinity as null
          if (matrix.get(i,j) == null) {
            continue;
          }

          if (!(matrix.get(i,j) < 2)) {
            throw new IllegalArgumentException("Invalid Coxeter system");
          }

        }
      }
    }
  }

  @Override
  public Word multiply(Word a, Word b) {
    return reduce(super.multiply(a,b));
  }

  private Word reduce(Word a) {
    return a;
  }

}
