import dk.jonaslindstrom.ruffini.permutations.algorithms.RandomDerangement;
import dk.jonaslindstrom.ruffini.permutations.elements.Permutation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class PermutationTests {

    @Test
    public void testRandomDerangement() {
        RandomDerangement randomDerangement = new RandomDerangement(new Random(1234));
        Permutation permutation = randomDerangement.apply(13);

        for (int i = 0; i < 13; i++) {
            Assert.assertTrue(permutation.apply(i) != i);
        }
    }

}
