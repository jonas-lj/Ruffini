package demo;

import demo.poseidon.Constants;
import demo.poseidon.Poseidon;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;

import java.math.BigInteger;
import java.util.List;

public class PoseidonOptimisation {

    public static void main(String[] arguments) {

        Field<BigInteger> field = new BigPrimeField(Poseidon.BN254_MODULUS);

        Matrix<BigInteger> matrix = Constants.getMatrix(15);
        Matrix<BigInteger> inverses = matrix.map(field::invert);

        // m_ij = s_i - t_j
        // m_ij - m_i{j+1} = t_{j+1} - t_j

        // t1-t0, t2-t1, t3-t2, ..., t{n-1} - t{n-2}

        // s0 + t1
        // s1 + t1



        System.out.println(field.subtract(inverses.get(3,5), inverses.get(4, 5)));
        System.out.println(field.subtract(inverses.get(3, 6), inverses.get(4, 6)));


    }

}
