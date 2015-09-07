/*
 * Poisson.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * The <a href="https://en.wikipedia.org/wiki/Poisson_distribution">
 * Poisson probability distribution.</a>
 * <p>
 * Use when you have events that can happen instantaneously
 * and at any time. The chance of two or more events happening
 * at the same time is effectively zero for a time span small
 * enough.
 * <p>
 * For example: Over the years, a certain street corner has noted an
 * average of 7 traffic accidents per month.  What is the probability that
 * there will be double the amount of accidents the following month.  Here our
 * lambda is 7, the average from which we base the likelihood
 * of traffic accidents happening in a month. The random variable is 14, double
 * the amount of accidents expected.
 * <p>
 * <pre>
 * double result = Poisson.probability(7,14);  // less than 1% chance.
 * </pre>
 * <p>
 * What is the chance there will be 3 or less accidents next month?
 * <pre>
 * double result = new Poisson(lessThanOrEqual, 7).getResult(3) // 8.2% chance
 * // or by not creating a Poisson object, using static methods
 * result = CumulativeOperation.lessThanOrEqual
 *                             .apply(3, y-&gt; Poisson.probability(7,y));
 * </pre>
 */
public class Poisson extends DiscreteProbability {

  private final double lambda;

  /**
   * Creates a representation of a Poisson probability distribution.
   *
   * @param rvOperation The cumulative operation to be applied
   * @param lambda      The average rate of success
   */
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

  @Override
  public double computeResult(int randomVariable) {
    return probability(lambda, randomVariable);
  }

  /**
   * The Poisson probability.
   * The equation for this probability is:
   * P(Y) = (lambda^Y) * (e^-lambda) / Y!
   *
   * @param lambda         Average rate of success
   * @param randomVariable how many successes of interest
   * @return The probability of this event.
   */
  public static double probability(double lambda, int randomVariable) {
    // split Eulers exponent into integral and fractional parts
    int integral = (int) lambda;
    double fractional = lambda - integral;
    //counters
    int lambdas = randomVariable;
    int denoms = randomVariable;

    // start with the fractional component, it's between 0 and 1.0
    double result = Math.exp(-1.0 * fractional);

    while (integral > 0 || lambdas > 0) {
      //lowering values
      if (result >= 1.0 || lambdas == 0) {
        if (denoms > 0) {
          result /= denoms;
          denoms--;
        } else {
          result *= Math.exp(-1.0);
          integral--;
        }
      } else { // increasing values
        result *= lambda;
        lambdas--;
      }
    }
    return result;
  }
}
