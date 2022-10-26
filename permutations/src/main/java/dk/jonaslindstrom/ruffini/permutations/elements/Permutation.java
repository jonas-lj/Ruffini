package dk.jonaslindstrom.ruffini.permutations.elements;

import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.matrices.elements.SparseMatrix;
import dk.jonaslindstrom.ruffini.common.util.PermutationUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.function.UnaryOperator;

public class Permutation implements UnaryOperator<Integer> {

    private final int[] p;

    public Permutation(Integer n) {
        // Identity permutation
        this.p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }
    }

    public Permutation(int n, int[] cycle) {
        this.p = new int[n];
        Arrays.fill(p, -1);
        for (int i = 0; i < cycle.length - 1; i++) {
            p[cycle[i]] = cycle[i + 1];
        }
        p[cycle[cycle.length - 1]] = cycle[0];

        for (int i = 0; i < n; i++) {
            if (p[i] < 0) {
                p[i] = i;
            }
        }
    }

    public Permutation(int... permutation) {
        this.p = permutation;
    }

    /**
     * Sample a random permutation uniformly among all permutations on n elements.
     */
    public static Permutation samplePermutation(int n, Random random) {

        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }

        for (int i = 0; i < n - 1; i++) {
            int j = random.nextInt(n - i);
            PermutationUtils.swap(p, i, j);
        }

        return new Permutation(p);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\\begin{pmatrix}\n");
        for (int i = 0; i < p.length; i++) {
            sb.append(i);
            if (i < p.length - 1) {
                sb.append(" & ");
            }
        }
        sb.append("\\\\ \n");

        for (int i = 0; i < p.length; i++) {
            sb.append(apply(i));
            if (i < p.length - 1) {
                sb.append(" & ");
            }
        }
        sb.append("\\\\ \n");
        sb.append("\\end{pmatrix}\n");
        return sb.toString();
    }

    @Override
    public Integer apply(Integer x) {
        if (x < 0 || x >= p.length) {
            throw new IllegalArgumentException();
        }
        return p[x];
    }

    public Matrix<Integer> getMatrixRepresentation() {
        SparseMatrix.Builder<Integer> builder = new SparseMatrix.Builder<>(p.length, p.length, 0);
        for (int i = 0; i < p.length; i++) {
            builder.add(i, apply(i), 1);
        }
        return builder.build();
    }

}
