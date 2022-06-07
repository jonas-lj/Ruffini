package dk.jonaslindstrom.math.util;

import java.util.List;

public class StringUtils {

  public static String superscript(String str) {
    str = str.replaceAll("0", "⁰");
    str = str.replaceAll("1", "¹");
    str = str.replaceAll("2", "²");
    str = str.replaceAll("3", "³");
    str = str.replaceAll("4", "⁴");
    str = str.replaceAll("5", "⁵");
    str = str.replaceAll("6", "⁶");
    str = str.replaceAll("7", "⁷");
    str = str.replaceAll("8", "⁸");
    str = str.replaceAll("9", "⁹");
    return str;
  }

  public static String subscript(String str) {
    str = str.replaceAll("0", "₀");
    str = str.replaceAll("1", "₁");
    str = str.replaceAll("2", "₂");
    str = str.replaceAll("3", "₃");
    str = str.replaceAll("4", "₄");
    str = str.replaceAll("5", "₅");
    str = str.replaceAll("6", "₆");
    str = str.replaceAll("7", "₇");
    str = str.replaceAll("8", "₈");
    str = str.replaceAll("9", "₉");
    return str;
  }

  public static String getWhiteSpaces(int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      sb.append(" ");
    }
    return sb.toString();
  }

  public static String sumToString(List<String> terms) {
    StringBuilder sb = new StringBuilder();
    sb.append(terms.get(0));

    for (int i = 1; i < terms.size(); i++) {
      if (terms.get(i).startsWith("-")) {
        sb.append(" - ");
        sb.append(terms.get(i).substring(1));
      } else {
        sb.append(" + ");
        sb.append(terms.get(i));
      }
    }
    return sb.toString();
  }
}
