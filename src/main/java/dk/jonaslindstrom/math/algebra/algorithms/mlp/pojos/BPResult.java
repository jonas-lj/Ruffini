package dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos;

import java.util.LinkedList;

public class BPResult {

  private LinkedList<LayerBPResult> bpResults = new LinkedList<>();

  /** Add to beginning of list */
  public void add(LayerBPResult result) {
    bpResults.addFirst(result);
  }

  public LayerBPResult get(int i) {
    return bpResults.get(i);
  }
}
