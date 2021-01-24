package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.algorithms.RungeKutta;
import dk.jonaslindstrom.math.algebra.concretisations.RealCoordinateSpace;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;
import javax.imageio.ImageIO;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;

/**
 * Compute a curve from the Lorenz system, the so-called "Butterfly" using Runge-Kutta numerical
 * integration.
 */
public class LorenzDemo {

  public static void main(String[] arguments) throws IOException {
    double s = 10.0;
    double b = 8.0 / 3.0;
    double r = 28.0;

    BiFunction<Double, Vector<Double>, Vector<Double>> f = (t, v) -> Vector.of(
        s * (v.get(1) - v.get(0)),
        v.get(0) * (r - v.get(2)) - v.get(1),
        v.get(0) * v.get(1) - b * v.get(2));

    BufferedImage bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = bi.createGraphics();
    graphics
        .setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setBackground(Color.WHITE);
    graphics.clearRect(0, 0, 1000, 1000);
    graphics.setColor(Color.BLACK);

    Vector<Double> p0 = Vector.of(1.0, 1.0, 1.0);
    RungeKutta<Vector<Double>> integrator = new RungeKutta<>(f, 0.0, p0,
        new RealCoordinateSpace(3));
    double h = 0.001;

    for (int i = 0; i < 100000; i++) {
      Vector<Double> p1 = integrator.step(h).second;
      int x0 = (int) mapToRange(p0.get(1), new Interval(-30, 30), new Interval(0, 1000));
      int y0 = (int) mapToRange(p0.get(2), new Interval(0, 50), new Interval(0, 1000));
      int x1 = (int) mapToRange(p1.get(1), new Interval(-30, 30), new Interval(0, 1000));
      int y1 = (int) mapToRange(p1.get(2), new Interval(0, 50), new Interval(0, 1000));
      graphics.drawLine(x0, y0, x1, y1);
      p0 = p1;
    }

    ImageIO.write(bi, "PNG", new File("out.png"));
  }

  static double mapToRange(double x, Interval domain, Interval image) {
    double x̄ = (x - domain.getInf()) / domain.getSize();
    return image.getInf() + x̄ * image.getSize();
  }

}