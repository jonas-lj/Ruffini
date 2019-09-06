package dk.jonaslindstrom.math.util;

public class MatrixIndex {

  public final int i, j;
  
  public MatrixIndex(int i, int j) {
    this.i = i;
    this.j = j;
  }
  
  public static MatrixIndex of(int i, int j) {
    return new MatrixIndex(i, j);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + i;
    result = prime * result + j;
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
    MatrixIndex other = (MatrixIndex) obj;
    if (i != other.i) {
      return false;
    }
    if (j != other.j) {
      return false;
    }
    return true;
  }
  
  
}
