package dk.jonaslindstrom.math.algebra.elements.word;

import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.math.util.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlgebraElement<E> implements Iterable<Pair<Word, E>> {

  private final Map<Word, E> terms;

  private AlgebraElement(Map<Word, E> terms) {
    this.terms = terms;
  }

  public static <F> AlgebraElement<F> fromWord(F coefficient, int ... powers) {
    return new AlgebraElement<F>(Map.of(new Word(powers), coefficient));
  }

  public static <F> AlgebraElement<F> empty() {
    return new AlgebraElement<>(Map.of());
  }

  @Override
  public Iterator<Pair<Word, E>> iterator() {
    return terms.keySet().stream().map(w -> new Pair<>(w, terms.get(w))).iterator();
  }

  public static class AlgebraElementBuilder<E> {

    private final Map<Word, E> terms;
    private final int variables;

    public AlgebraElementBuilder(int variables) {
      this.terms = new HashMap<>();
      this.variables = variables;
    }

    public AlgebraElementBuilder<E> setTerm(Word word, E coefficient) {
      this.terms.put(word, coefficient);
      return this;
    }

    public AlgebraElement<E> build() {
      return new AlgebraElement<>(terms);
    }
  }


  public <F> AlgebraElement<F> map(Function<E, F> map) {
    return new AlgebraElement<F>(terms.keySet().stream().parallel().collect(
        Collectors.toMap(w -> w, w -> map.apply(terms.get(w)))));
  }

  public AlgebraElement<E> add(AlgebraElement<E> other, BinaryOperator<E> addition) {
    Map<Word, E> newTerms = new HashMap<>();
    
    for (Word w : this.terms.keySet()) {
      newTerms.put(w, this.terms.get(w));
    }

    for (Word w : other.terms.keySet()) {
      if (newTerms.containsKey(w)) {
        newTerms.put(w, addition.apply(this.terms.get(w), other.terms.get(w)));
      } else {
        newTerms.put(w, other.terms.get(w));
      }
    }

    return new AlgebraElement<>(newTerms);
  }

  public AlgebraElement<E> multiply(AlgebraElement<E> other,
      BinaryOperator<E> multiplication, BinaryOperator<E> addition) {

    return new AlgebraElement<>(this.terms.keySet().stream().flatMap(
        term -> other.terms.keySet().stream().map(otherTerm -> Pair.of(term, otherTerm))
    ).collect(Collectors.toMap(
        pair -> pair.getFirst().multiply(pair.getSecond()),
        pair -> multiplication.apply(terms.get(pair.first), other.terms.get(pair.second)),
        addition)));
  }

  public boolean equals(AlgebraElement<E> other, BiPredicate<E, E> equals) {
    if (!this.terms.keySet().equals(other.terms)) {
      return false;
    }

    return this.terms.keySet().stream().parallel().allMatch(w -> equals.test(this.terms.get(w), other.terms.get(w)));
  }

  @Override
  public String toString() {
    List<String> termsAsString = terms.keySet().stream().map(word -> {
      String coefficient = terms.get(word).toString();
      if (coefficient.equals("1")) {
        return word.toString();
      }
      return  coefficient + " " + word.toString();
    }).collect(Collectors.toList());
    return StringUtils.sumToString(termsAsString);
  }

}
