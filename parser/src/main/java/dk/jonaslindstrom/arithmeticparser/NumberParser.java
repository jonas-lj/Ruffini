package dk.jonaslindstrom.arithmeticparser;

@FunctionalInterface
public interface NumberParser<T> {

  T parse(String s) throws NumberFormatException;
  
}
