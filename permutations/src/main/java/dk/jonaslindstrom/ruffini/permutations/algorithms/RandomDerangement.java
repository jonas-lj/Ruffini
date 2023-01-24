package dk.jonaslindstrom.ruffini.permutations.algorithms;

import dk.jonaslindstrom.ruffini.permutations.elements.Permutation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomDerangement {

  private final Random random;

  public RandomDerangement(Random random) {
    this.random = random;
  }

  /** Algorithm from "Generating random derangements" by Martínez et.al (https://epubs.siam.org/doi/pdf/10.1137/1.9781611972986.7). */
  public Permutation apply(int n) {

    int[] permutation = IntStream.range(0, n).toArray();
    boolean[] mark = new boolean[n];

    int i = n - 1;
    int u = n - 1;

    while (u > 0) {
      if (!mark[i]) {
        int j;
        do {
          j = random.nextInt(i);
        } while (mark[j]);

        swap(permutation, i, j);

        double p = random.nextDouble();
        double bound = D(u-1) * u / D(u+1);
        if (p < bound) {
          mark[j] = true;
          u--;
        }
        u--;
      }
      i--;
    }
    return new Permutation(permutation);
  }

  /** Compute the number of derangements of n symbols recursively */
  public static double D(int n) {
    if (n == 0) {
      return 1;
    } else if (n == 1) {
      return 0;
    }
    return (n-1) * (D(n-1) + D(n-2));
  }

  /** Swap the i'th and j'th entry in the given array */
  private static void swap(int[] array, int i, int j) {
    int tmp = array[i];
    array[i] = array[j];
    array[j] = tmp;
  }

}
