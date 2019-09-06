package dk.jonaslindstrom.math.algebra.concretisations;

public class Rationals extends FieldOfFractions<Integer> {

  private static Rationals instance = new Rationals();
  
  public static Rationals getInstance() {
    return instance;
  }
  
  private Rationals() {
    super(Integers.getInstance());
  }

  @Override
  public String toString() {
    return "ℚ";
  }

}
