package dk.jonaslindstrom.math.algebra.elements.matrix;

/**
 * A view into a subset of the rows and columns of another matrix.
 *
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 */
class MatrixView<E> extends ConstructiveMatrix<E> {

  public MatrixView(Matrix<E> matrix) {
    super(matrix.getHeight(), matrix.getWidth(), matrix::get);
  }

}
