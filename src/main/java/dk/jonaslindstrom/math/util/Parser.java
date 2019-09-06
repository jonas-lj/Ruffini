package dk.jonaslindstrom.math.util;

import dk.jonaslindstrom.math.algebra.elements.Fraction;

public class Parser {

  public static Fraction<Integer> parseFraction(String string) {
    String[] split = string.split("/");
    Integer n = Integer.parseInt(split[0].trim());
    Integer d = Integer.parseInt(split[1].trim());
    return new Fraction<>(n, d);
  }



}
