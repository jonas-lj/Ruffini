package dk.jonaslindstrom.math;



import dk.jonaslindstrom.math.util.ArrayUtils;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class TestArrayUtils {

  @Test
  public void testUnion() {
    int[] a = new int[] {1, 3, 5};
    int[] b = new int[] {3, 4};

    int[] e = new int[] {1, 3, 4, 5};
    Assert.assertArrayEquals(e, ArrayUtils.union(a, b));
  }

  @Test
  public void testSubsets() {
    ArrayUtils.subsets(5).forEach(s -> System.out.println(Arrays.toString(s)));
    Assert.assertEquals(32, ArrayUtils.subsets(5).count());
  }

  @Test
  public void testRemove() {
    int[] a = new int[] {1,2,3,4,5};
    int[] e = new int[] {1,2,3,5};
    Assert.assertArrayEquals(e, ArrayUtils.remove(a, 3));
    
  }
}
