/*
 * Poisson.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * User: Joe Nellis
 * Date: 8/21/2015
 * Time: 10:33 AM
 */
public class Poisson extends DiscreteProbability {

  private final double lambda;

  public Poisson(CumulativeOperation rvOperation,
                 double lambda) {
    super(rvOperation);
    this.lambda = lambda;
  }

  @Override
  public double getExpectedValue() {
    return lambda;
  }

  @Override
  public double getVariance() {
    return lambda;
  }

  /*
  The Poisson probability.
    The equation for this probability is:
    P(Y) = lambda^Y * e^-lambda / Y!
  */
  @Override
  public double computeResult(int randomVariable) {
    // split eulers exponent into integral and fractional parts
    int integral = (int) lambda;
    double fractional = lambda - integral;
    //counters
    int lambdas = randomVariable;
    int denoms = randomVariable;

    // start with the fractional component, it's between 0 and 1.0
    double result = Math.exp(-1.0 * fractional);

    while (integral > 0 || lambdas > 0 || denoms > 0) {
      //lowering values
      if (result >= 1.0 || lambdas == 0) {
        if (denoms > 0) {
          result /= denoms;
          denoms--;
        } else if (integral > 0) {
          result *= Math.exp(-1.0);
          integral--;
        } else {
          throw new IllegalStateException("unexpected end of small values");
        }
      } else { // increasing values
        if (lambdas > 0) {
          result *= lambda;
          lambdas--;
        } else {
          throw new IllegalStateException("premature end of power component");
        }
      }
    }
    return result;
  }
}
