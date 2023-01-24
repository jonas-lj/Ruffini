package dk.jonaslindstrom.ruffini.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayUtils {


    public static int[] prepend(int a, int[] list) {
        int[] out = new int[list.length + 1];
        out[0] = a;
        System.arraycopy(list, 0, out, 1, list.length);
        return out;
    }

    public static int[] append(int a, int[] list) {
        int[] out = new int[list.length + 1];
        out[list.length] = a;
        System.arraycopy(list, 0, out, 0, list.length);
        return out;
    }

    public static int[] concat(int[] a, int[] b) {
        int[] out = new int[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }

    /**
     * Return a new array which is a copy of the original one except that the element with index i is
     * excluded.
     *
     * @param list
     * @param i
     * @return
     */
    public static int[] remove(int[] list, int i) {
        assert (i >= 0 && i < list.length);
        int[] out = new int[list.length - 1];
        System.arraycopy(list, 0, out, 0, i);
        System.arraycopy(list, i + 1, out, i, list.length - i - 1);
        return out;
    }

    /**
     * Return a new list where a list of elements are not included. It is assumes that both the list
     * and the list of elements to be removed are sorted and that all the elements to be removed are
     * in the original list.
     *
     * @param list
     * @param elements
     * @return
     */
    public static int[] removeElements(int[] list, int[] elements) {

        if (elements.length > list.length) {
            throw new IllegalArgumentException(
                    "Cannot remove more elements than are in the original set");
        }

        int[] out = new int[list.length - elements.length];

        int j = 0;
        int k = 0;
        for (int i = 0; i < list.length; i++) {
            if (j < elements.length && list[i] == elements[j]) {
                j++;
            } else {

                if (k >= out.length) {
                    throw new IllegalArgumentException("Not all elements are in original list");
                }

                out[k++] = list[i];
            }
        }
        return out;
    }

    /**
     * Returns a new array which contains all elements from the original array except for a given set
     * of indices. It is assumed that the list and list of indices are sorted and that all indices are
     * in the range.
     *
     * @param list
     * @param indices
     * @return
     */
    public static int[] remove(int[] list, int[] indices) {
        int[] out = new int[list.length - indices.length];
        int j = 0;
        int k = 0;
        for (int i = 0; i < list.length; i++) {
            if (j < indices.length && i == indices[j]) {
                j++;
            } else {
                out[k++] = list[i];
            }
        }
        return out;
    }

    public static <E> ArrayList<E> populate(int n, IntFunction<E> populator) {
        return IntStream.range(0, n).mapToObj(populator)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Return all subsets of the array in lexicographical order of the indices.
     *
     * @param set
     * @return
     */
    public static <E> Stream<List<E>> subsets(List<E> set) {
        return subsets(set.size()).map(subset -> sublist(set, subset));
    }

    public static Stream<int[]> subsets(int[] set) {
        return subsets(set.length).map(subset -> sublist(set, subset));
    }


    /**
     * Return all subsets of {0,1,2,...,r-1} lexicographical order.
     *
     * @param r
     * @return
     */
    public static Stream<int[]> subsets(int r) {

        Stream<int[]> stream = Stream.of(new int[0]);
        if (r == 0) {
            return stream;
        }

        return Stream.concat(stream,
                IntStream.range(0, r).boxed().flatMap(
                        i -> subsets(r - i - 1).map(set -> Arrays.stream(set).map(x -> x + i + 1).toArray())
                                .map(s -> ArrayUtils.prepend(i, s))));
    }

    /**
     * Return all nonempty subsets of {0,1,2,...,r-1} lexicographical order.
     *
     * @param r
     * @return
     */
    public static Stream<int[]> nonEmptySubsets(int r) {
        return subsets(r).filter(s -> s.length > 0);
    }

    public static Stream<int[]> nonEmptySubsets(int[] set) {
        return nonEmptySubsets(set.length).map(subset -> sublist(set, subset));
    }

    /**
     * Return a stream of all k-subsets of {0,1,2,...,r-1} in lexicographical order
     *
     * @param k
     * @param r
     * @return
     */
    public static Stream<int[]> ksubsets(int k, int r) {
        if (k == 0) {
            return Stream.of(new int[0]);
        } else if (k == 1) {
            return IntStream.range(0, r).mapToObj(i -> new int[]{i});
        }

        return IntStream.range(0, r).boxed()
                .flatMap(i -> ksubsets(k - 1, r - i - 1)
                        .map(set -> Arrays.stream(set).map(x -> x + i + 1).toArray())
                        .map(s -> ArrayUtils.prepend(i, s)));
    }

    public static <E> List<E> sublist(List<E> list, int[] indices) {
        return Arrays.stream(indices).mapToObj(list::get).collect(Collectors.toList());
    }

    public static int[] sublist(int[] list, int[] indices) {
        return Arrays.stream(indices).map(i -> list[i]).toArray();
    }

    public static <E> Stream<List<E>> ksubsets(int k, List<E> set) {
        return ksubsets(set.size(), k).map(s -> sublist(set, s));
    }

    public static Stream<int[]> ksubsets(int k, int[] set) {
        return ksubsets(k, set.length).map(s -> sublist(set, s));
    }

    /**
     * Return a list with all the elements of a and b occuring exactly once. Assumes that both arrays
     * are sorted.
     *
     * @param I
     * @param J
     * @return
     */
    public static int[] union(int[] I, int[] J) {

        int i, j, s;
        i = j = s = 0;

        int[] S = new int[I.length + J.length];

        while (i < I.length || j < J.length) {
            if (i < I.length && j < J.length) {
                if (I[i] < J[j]) {
                    S[s++] = I[i++];
                } else if (I[i] > J[j]) {
                    S[s++] = J[j++];
                } else {
                    S[s++] = I[i++];
                    j++;
                }
            } else {
                int l;
                if (i < I.length) {
                    l = I.length - i;
                    System.arraycopy(I, i, S, s, l);
                    i += l;
                } else {
                    l = J.length - j;
                    System.arraycopy(J, j, S, s, l);
                    j += l;
                }
                s += l;
            }
        }

        S = Arrays.copyOfRange(S, 0, s);
        return S;
    }

    /**
     * Return a copy of the given array with the entries in reversed order
     */
    public static byte[] reverse(byte[] bytes) {
        byte[] reversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversed[i] = bytes[bytes.length - i - 1];
        }
        return reversed;
    }
}
