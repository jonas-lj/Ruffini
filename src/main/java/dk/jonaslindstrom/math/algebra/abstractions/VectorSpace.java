package dk.jonaslindstrom.math.algebra.abstractions;

public abstract class VectorSpace<Vectors, Scalars> extends Module<Vectors, Scalars> {

  public VectorSpace(AdditiveGroup<Vectors> vectors, Field<Scalars> scalars) {
    super(vectors, scalars);
  }

  @Override
  public Field<Scalars> getScalars() {
    return (Field<Scalars>) this.scalars;
  }

}
