package dk.jonaslindstrom.math.util;

public class MathUtils {

  public static int product(int ... factors) {
    
    if (factors.length == 0) {
      return 1;
    }
    
    int result = factors[0];
    for (int i = 1; i < factors.length; i++) {
      result *= factors[i];
    }
    return result;
  }
  
  public static int nextPowerOfTwo(int n) {
    n--;
    n |= n >> 1;
    n |= n >> 2;
    n |= n >> 4;
    n |= n >> 8;
    n |= n >> 16;
    n++;
    return n;
  }
}
