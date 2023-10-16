package demo.poseidon;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingField;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Poseidon {

    // Bn254
    public static BigInteger BN254_MODULUS = new BigInteger("21888242871839275222246405745257275088548364400416034343698204186575808495617");

    public static void main(String[] arguments) {
        Map<List<BigInteger>, BigInteger> testVector = Map.of(
//                List.of(new BigInteger("1")), new BigInteger("18586133768512220936620570745912940619677854269274689475585506675881198879027"),
                List.of(new BigInteger("1"), new BigInteger("2")), new BigInteger("7853200120776062878684798364095072458815029376092732009249414926327459813530")
//                Stream.of(1, 2, 0, 0, 0).map(BigInteger::valueOf).collect(Collectors.toList()), new BigInteger("1018317224307729531995786483840663576608797660851238720571059489595066344487"),
//                Stream.of(1, 2, 0, 0, 0, 0).map(BigInteger::valueOf).collect(Collectors.toList()), new BigInteger("15336558801450556532856248569924170992202208561737609669134139141992924267169"),
//                Stream.of(3, 4, 0, 0, 0).map(BigInteger::valueOf).collect(Collectors.toList()), new BigInteger("5811595552068139067952687508729883632420015185677766880877743348592482390548"),
//                Stream.of(3, 4, 0, 0, 0, 0).map(BigInteger::valueOf).collect(Collectors.toList()), new BigInteger("12263118664590987767234828103155242843640892839966517009184493198782366909018"),
//                Stream.of(1, 2, 3, 4, 5, 6).map(BigInteger::valueOf).collect(Collectors.toList()), new BigInteger("20400040500897583745843009878988256314335038853985262692600694741116813247201"),
//                Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14).map(BigInteger::valueOf).collect(Collectors.toList()), new BigInteger("8354478399926161176778659061636406690034081872658507739535256090879947077494")
        );

        for (List<BigInteger> inputs : testVector.keySet()) {
            BigInteger actual = PoseidonHash.poseidonBn254Hash(inputs);
            System.out.println(inputs + " - " + actual.equals(testVector.get(inputs)));
        }
    }

    public static class PoseidonHash<E> {

        private final Matrix<E> m;
        private final List<E> c;
        private final int fullRounds;
        private final int partialRounds;
        private final Field<E> field;
        private final Power<E> power;
        private ArrayList<E> state = new ArrayList<>();
        public PoseidonHash(Matrix<E> m,
                            List<E> c,
                            int fullRounds,
                            int partialRounds,
                            Field<E> field) {
            this.m = m;
            this.c = c;
            this.fullRounds = fullRounds;
            this.partialRounds = partialRounds;
            this.field = field;
            this.power = new Power<>(field);
        }

        public static BigInteger poseidonBn254Hash(List<BigInteger> inputs) {
            return PoseidonHash.poseidonBn254(inputs.size()).hash(inputs);
        }

        public static PoseidonHash<BigInteger> poseidonBn254(int inputs) {
            int t = inputs - 1;
            List<BigInteger> c = Constants.getConstants(t);
            Matrix<BigInteger> m = Constants.getMatrix(t);
            int fullRounds = Constants.getFullRounds(t);
            int partialRounds = Constants.getPartialRounds(t);
            return new PoseidonHash<>(m, c, fullRounds, partialRounds, new BigPrimeField(BN254_MODULUS));
        }

        private void ark(int it) {
            for (int i = 0; i < state.size(); i++) {
                state.set(i, this.field.add(state.get(i), c.get(it + i)));
            }
        }

        private void sbox(int i) {
            if (i < fullRounds / 2 || i >= fullRounds / 2 + partialRounds) {
                state.replaceAll(a -> power.apply(a, 5));
            } else {
                state.set(0, power.apply(state.get(0), 5));
            }
        }

        private void mix() {
            this.state = new ArrayList<>(m.apply(Vector.ofList(state), field).asList());
        }

        public E hash(List<E> inputs) {
            int t = inputs.size() + 1;

            if (t != m.getHeight()) {
                throw new IllegalArgumentException("Invalid number of inputs. Was " + inputs.size() + " but should be " + (t - 1));
            }

            state.clear();
            state.add(field.zero());
            state.addAll(inputs);

            for (int i = 0; i < fullRounds + partialRounds; i++) {
                ark(i * t);
                sbox(i);
                mix();
                System.out.println(state);
            }
            return state.get(0);
        }
    }

}
