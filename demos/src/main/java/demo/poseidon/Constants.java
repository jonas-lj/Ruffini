package demo.poseidon;

import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Constants {

    public static List<BigInteger> getConstants(int t) {
        try {
            return getCStrings(t).map(BigInteger::new).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<String> getCStrings(int t) throws IOException {

        Map<Integer, String> files = new HashMap<>();
        files.put(0, "poseidon_constants/constants00");
        files.put(1, "poseidon_constants/constants01");
        files.put(2, "poseidon_constants/constants02");
        files.put(3, "poseidon_constants/constants03");
        files.put(4, "poseidon_constants/constants04");
        files.put(5, "poseidon_constants/constants05");
        files.put(6, "poseidon_constants/constants06");
        files.put(7, "poseidon_constants/constants07");
        files.put(8, "poseidon_constants/constants08");
        files.put(9, "poseidon_constants/constants09");
        files.put(10, "poseidon_constants/constants10");
        files.put(11, "poseidon_constants/constants11");
        files.put(12, "poseidon_constants/constants12");
        files.put(13, "poseidon_constants/constants13");
        files.put(14, "poseidon_constants/constants14");
        files.put(15, "poseidon_constants/constants15");

        if (files.containsKey(t)) {
            File file = new File(files.get(t));
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Matrix<BigInteger> getMatrix(int t) {
        try {
            List<String> rows = getMatrixStrings(t);
            int n = t + 2;
            return Matrix.of(n, n, (i,j) -> new BigInteger(rows.get(i * n + j)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getMatrixStrings(int i) throws IOException {

        Map<Integer, String> files = new HashMap<>();
        files.put(0, "poseidon_constants/matrix00");
        files.put(1, "poseidon_constants/matrix01");
        files.put(2, "poseidon_constants/matrix02");
        files.put(3, "poseidon_constants/matrix03");
        files.put(4, "poseidon_constants/matrix04");
        files.put(5, "poseidon_constants/matrix05");
        files.put(6, "poseidon_constants/matrix06");
        files.put(7, "poseidon_constants/matrix07");
        files.put(8, "poseidon_constants/matrix08");
        files.put(9, "poseidon_constants/matrix09");
        files.put(10, "poseidon_constants/matrix10");
        files.put(11, "poseidon_constants/matrix11");
        files.put(12, "poseidon_constants/matrix12");
        files.put(13, "poseidon_constants/matrix13");
        files.put(14, "poseidon_constants/matrix14");
        files.put(15, "poseidon_constants/matrix15");

        if (files.containsKey(i)) {
            File file = new File(files.get(i));
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines().toList();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static int getFullRounds(int t) {
        return 8;
    }

    public static int getPartialRounds(int t) {
        return List.of(56, 57, 56, 60, 60, 63, 64, 63, 60, 66, 60, 65, 70, 60, 64, 68).get(t);
    }
}
