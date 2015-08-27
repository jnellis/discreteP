package net.jnellis.probability;

/**
 * User: Joe Nellis
 * Date: 8/21/2015
 * Time: 8:18 PM
 */
public class Geometric extends DiscreteProbability {

  private final double p;

  public Geometric(CumulativeOperation rvOperation,
                   double chanceOfSuccess) {
    super(rvOperation);
    this.p = chanceOfSuccess;
  }

  // Expected value or population mean is defined by
  // E(Y) = 1/p
  @Override
  double getExpectedValue() {
    return p == 0.0 ? Double.POSITIVE_INFINITY : Probability.reciprocal(p);
  }


  // Variance is defined by
  // V(Y)= (1-p)/p^2
  @Override
  double getVariance() {
    return p == 0.0 ? Double.POSITIVE_INFINITY : (1.0 - p) / (p * p);
  }

  @Override
  public double computeResult(int randomVariable) {
    return randomVariable == 0 ? 0.0
                               : Math.pow((1.0 - p), randomVariable - 1.0d) * p;
  }
}
