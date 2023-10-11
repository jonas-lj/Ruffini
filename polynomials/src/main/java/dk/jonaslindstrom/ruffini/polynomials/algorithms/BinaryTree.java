package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

import static dk.jonaslindstrom.ruffini.common.util.MathUtils.floorLog2;
import static dk.jonaslindstrom.ruffini.common.util.MathUtils.isPowerOfTwo;

/**
 * This represents a labeled binary tree where the label of an internal node is the result of an operation on the labels
 * of its children.
 */
public class BinaryTree<L> {
    private final Node root;
    private final BinaryOperator<L> operator;

    public BinaryTree(List<L> leafs, BinaryOperator<L> operator) {

        if (!isPowerOfTwo(leafs.size())) {
            throw new IllegalArgumentException("Number of leafs must be a power of two");
        }
        this.operator = operator;
        this.root = new Node(leafs);
    }

    public List<L> evaluate(L value, BinaryOperator<L> operator) {
        return root.evaluateFromRoot(value, operator);
    }

    public L getRootLabel() {
        return root.label;
    }

    private class Node {

        final Node left, right;
        private final L label;

        private Node(List<L> leafs) {
            this(floorLog2(leafs.size()), 0, leafs);
        }

        private Node(int d, int i, List<L> leafs) {
            if (d == 0) {
                this.left = null;
                this.right = null;
                this.label = leafs.get(i);
            } else {
                this.left = new Node(d - 1, 2 * i, leafs);
                this.right = new Node(d - 1, 2 * i + 1, leafs);
                this.label = operator.apply(left.label, right.label);
            }
        }

        private boolean isLeaf() {
            return left == null && right == null;
        }

        private List<L> evaluateFromRoot(L parent, BinaryOperator<L> operator) {
            L value = operator.apply(parent, this.label);
            if (isLeaf()) {
                return List.of(value);
            }
            List<L> results = new ArrayList<>();
            results.addAll(left.evaluateFromRoot(value, operator));
            results.addAll(right.evaluateFromRoot(value, operator));
            return results;
        }
    }
}
