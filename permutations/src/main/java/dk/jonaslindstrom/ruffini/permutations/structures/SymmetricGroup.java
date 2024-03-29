package dk.jonaslindstrom.ruffini.permutations.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Group;
import dk.jonaslindstrom.ruffini.permutations.elements.Permutation;

import java.util.Objects;

public class SymmetricGroup implements Group<Permutation> {

    private final int n;

    public SymmetricGroup(int n) {
        this.n = n;
    }

    @Override
    public Permutation identity() {
        return new Permutation(n);
    }

    @Override
    public Permutation multiply(Permutation a, Permutation b) {
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = a.apply(b.apply(i));
        }
        return new Permutation(p);
    }

    @Override
    public String toString(Permutation a) {
        return a.toString();
    }

    @Override
    public boolean equals(Permutation a, Permutation b) {
        for (int i = 0; i < n; i++) {
            if (!Objects.equals(a.apply(i), b.apply(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Permutation invert(Permutation a) {
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[a.apply(i)] = i;
        }
        return new Permutation(p);
    }

    @Override
    public String toString() {
        return "S_{" + n + "}";
    }

}
