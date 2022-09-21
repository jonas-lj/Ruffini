package dk.jonaslindstrom.math.algebra.elements.word;

import java.util.Arrays;
import java.util.Iterator;

public class Word implements Iterable<Integer> {

  private final int[] word;

  public Word(int... word) {
    this.word = word;
  }

  public Word multiply(Word other) {
    int[] combined = new int[this.word.length + other.word.length];
    System.arraycopy(this.word, 0, combined, 0, this.word.length);
    System.arraycopy(other.word, 0, combined, this.word.length, other.word.length);
    return new Word(combined);
  }

  @Override
  public Iterator<Integer> iterator() {
    return Arrays.stream(word).iterator();
  }

  public boolean equals(Word other) {
    return Arrays.equals(word, other.word);
  }

  public String toString() {
    if (word.length == 0) {
      return "e"; // Empty word
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < word.length; i++) {
      if (i > 0) {
        sb.append(" ");
      }
      sb.append("x_{").append(word[i]).append("}");

      // Compute power
      int j;
      for (j = 1; j < word.length - i; j++) {
        if (word[i + j] != word[i]) {
          break;
        }
      }

      if (j > 1) {
        sb.append("^{").append(j).append("}");
        i += j - 1;
      }
    }
    return sb.toString();
  }

}
