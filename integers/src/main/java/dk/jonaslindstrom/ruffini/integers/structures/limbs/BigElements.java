package dk.jonaslindstrom.ruffini.integers.structures.limbs;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BigElements<E> implements Ring<BigElement<E>> {

    private final EuclideanDomain<E> ring;
    private final E modulus;

    public BigElements(EuclideanDomain<E> ring, E modulus) {
        this.ring = ring;
        this.modulus = modulus;
    }

    @Override
    public BigElement<E> negate(BigElement<E> a) {
        return new BigElement<>(a.limbs.stream().map(ai -> ring.subtract(modulus, ai)).toList());
    }

    @Override
    public BigElement<E> add(BigElement<E> a, BigElement<E> b) {
        E carry = ring.zero();
        List<E> limbs = new ArrayList<>();
        for (int i = 0; i < Math.max(a.limbs.size(), b.limbs.size()); i++) {
            E ai = i < a.limbs.size() ? a.limbs.get(i) : ring.zero();
            E bi = i < b.limbs.size() ? b.limbs.get(i) : ring.zero();
            E di = ring.add(ai, bi, carry);
            Pair<E, E> qr = ring.divide(di, modulus);
            carry = qr.first;
            limbs.add(qr.second);
        }
         if (!ring.isZero(ring.zero())) {
             limbs.add(carry);
         }
        return new BigElement<>(limbs);
    }

    @Override
    public BigElement<E> zero() {
        return new BigElement<>(List.of(ring.zero()));
    }

    @Override
    public BigElement<E> identity() {
        return new BigElement<>(List.of(ring.identity()));
    }

    @Override
    public BigElement<E> multiply(BigElement<E> a, BigElement<E> b) {
        return null;
    }

    @Override
    public boolean equals(BigElement<E> a, BigElement<E> b) {
        if (a.limbs.size() != b.limbs.size()) {
            return false;
        }
        for (int i = 0; i < a.limbs.size(); i++) {
            if (!ring.equals(a.limbs.get(i), b.limbs.get(i))) {
                return false;
            }
        }
        return true;
    }
}
