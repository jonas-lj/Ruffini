package dk.jonaslindstrom.ruffini.common.util;


public class PermutationUtils {

    public static void swap(int[] list, int i, int j) {
        int tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }

    public static void swap(Object[] list, int i, int j) {
        Object tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }

}
