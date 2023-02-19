package dk.jonaslindstrom.arithmeticparser;

/**
 * Instances of this class represents a token in an arithmetic expression.
 */
public class Token {

  public enum Type {
    FUNCTION, NUMBER, OPERATOR, VARIABLE, LEFT_PARANTHESIS;
  }

  private final String representation;
  private final Type type;
  
  public Token(String representation, Type type) {
    this.representation = representation;
    this.type = type;
  }

  public String getRepresentation() {
    return representation;
  }

  public Type getType() {
    return type;
  }
  
  public String toString() {
    return representation;
  }
  
}
