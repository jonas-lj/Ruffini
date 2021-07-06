package dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos;

import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.ArrayList;
import java.util.List;

public class FPResult {

  private List<LayerFPResult> outputs = new ArrayList<>();

  public void add(LayerFPResult result) {
    outputs.add(result);
  }

  public Vector<Double> getOutput() {
    return outputs.get(outputs.size() - 1).getAfterActivation();
  }

  public LayerFPResult get(int i) {
    return outputs.get(i);
  }


}
