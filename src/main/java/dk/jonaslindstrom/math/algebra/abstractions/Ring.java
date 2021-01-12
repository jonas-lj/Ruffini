package dk.jonaslindstrom.math.algebra.abstractions;

public interface Ring<E> extends SemiRing<E>, AdditiveGroup<E> {

  int getCharacteristics();

}
