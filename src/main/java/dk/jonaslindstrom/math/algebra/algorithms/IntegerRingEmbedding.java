package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import java.util.function.IntFunction;

/**
 * This function computes the canonical embedding of the integers into any ring by mapping an
 * integer <i>n</i> to the identity element added to it self <i>n</i> times.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public class IntegerRingEmbedding<E> implements IntFunction<E> {

  private Ring<E> ring;

  public IntegerRingEmbedding(Ring<E> ring) {
    this.ring = ring;
  }
  
  @Override
  public E apply(int a) {
    E b = ring.getZero();
    E p = ring.getIdentity();
    E two = ring.add(ring.getIdentity(), ring.getIdentity());
    
    while (a > 0) {
      if ((a & 1) != 0) {
        b = ring.add(b, p);
      }
      a = a >> 1;
      
      if (a > 0) {
        p = ring.multiply(two, p);        
      }
    }
    return b;
  }
  
}
