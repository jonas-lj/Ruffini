package dk.jonaslindstrom.math.util;

import dk.jonaslindstrom.math.algebra.elements.Permutation;
import java.util.Random;

public class PermutationUtils {

  public static void swap(int[] list, int i, int j) {
    int tmp = list[i];
    list[i] = list[j];
    list[j] = tmp;
  }

  public static void swap(Object[] list, int i, int j) {
    Object tmp = list[i];
    list[i] = list[j];
    list[j] = tmp;
  }

  /**
   * Sample a random permutation uniformly among all permutations on n elements.
   */
  public static Permutation samplePermutation(int n, Random random) {

    int[] p = new int[n];
    for (int i = 0; i < n; i++) {
      p[i] = i;
    }

    for (int i = 0; i < n - 1; i++) {
      int j = random.nextInt(n - i);
      PermutationUtils.swap(p, i, j);
    }

    return new Permutation(p);
  }

}
