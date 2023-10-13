package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A binary tree where the leafs are x-l_i for a set of constants (l_0, ..., l_{n-1}) and internal nodes are the product
 * of their children.
 */
public class SubproductTree<E> extends BinaryTree<Polynomial<E>> {

    private final PolynomialRingOverRing<E> polynomialRing;

    public SubproductTree(List<E> leafs, PolynomialRingOverRing<E> polynomialRing) {
        super(leafs.stream().map(leaf -> Polynomial.of(
                polynomialRing.getRing().negate(leaf),
                polynomialRing.getRing().identity())).collect(Collectors.toList()), polynomialRing::multiply);
        this.polynomialRing = polynomialRing;
    }

    /**
     * Evaluate from the root. For each node set the value to be the value from the parent modulo the label on the node.
     * Return the values from the leafs.
     */
    public List<E> evaluate(Polynomial<E> polynomial) {
        return super.evaluate(polynomial, new Remainder<>(polynomialRing)).stream().map(Polynomial::getConstant).toList();
    }
}
