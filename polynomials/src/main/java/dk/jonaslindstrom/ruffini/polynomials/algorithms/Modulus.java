//package dk.jonaslindstrom.ruffini.polynomials.algorithms;
//
//import dk.jonaslindstrom.ruffini.common.util.Pair;
//import dk.jonaslindstrom.ruffini.common.vector.Vector;
//import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
//import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingFFT;
//import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;
//
//import java.util.function.BiFunction;
//import java.util.function.UnaryOperator;
//
///**
// * Algorthm 9.5 from Modern Computer Algebra. This is asymptotically faster than the usual division algorithm, and in
// * particular if the degree of the divisor is almost as large as the degree of the dividend.
// */
//public class Modulus<E> implements UnaryOperator<Polynomial<E>> {
//
//    private final PolynomialRingFFT<E> ring;
//    private final PolynomialRingFFT<E>.TransformedPolynomial b;
//    private final Vector<PolynomialRingFFT<E>.TransformedPolynomial> inverses;
//
//    public Modulus(PolynomialRingFFT<E> ring, PolynomialRingFFT<E>.TransformedPolynomial b, int maxDegree) {
//        this.ring = ring;
//        this.b = b;
//        // Compute the inverse of b(x) mod x^{m+1}
//        this.inverses = Vector.of(maxDegree, i -> ring.fromPolynomial(new Inversion<>(ring.getField()).apply(ring.toPolynomial(b).reverse(), i+1)));
//    }
//
//    @Override
//    public Polynomial<E> apply(Polynomial<E> a) {
//        if (a.degree() < b.degree()) {
//            return a;
//        }
//
//        int m = a.degree() - b.degree();
//
//        Polynomial<E> bReverseInverse = inverses.get(m + b.degree());
//
//        Polynomial.Builder<E> builder = new Polynomial.Builder<>(ring.getRing());
//        a.reverse().forEach((i, ai) -> {
//            bReverseInverse.forEach((j, bj) -> {
//                if (i+j < m+1) {
//                    builder.addTo(i + j, ring.getRing().multiply(ai, bj));
//                }
//            });
//        });
//        Polynomial<E> q = builder.build().reverse();
//
//        return ring.subtract(a, ring.multiply(b, q));
//    }
//
//    @Override
//    public Polynomial<E> apply(Polynomial<E> polynomial) {
//        return null;
//    }
//}
