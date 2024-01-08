package dk.jonaslindstrom.ruffini.integers.structures.limbs;

import java.util.List;

public class BigElement<E> {

    final List<E> limbs;

    public BigElement(List<E> limbs) {
        this.limbs = limbs;
    }

    public String toString() {
        return limbs.toString();
    }
}
