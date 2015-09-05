/*
 * DiscreteProbability.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

import java.util.Objects;

/**
 * An abstract class that represents a discrete probability distribution.
 */
public abstract class DiscreteProbability implements Probability  {

  private final CumulativeOperation rvOperation;

  /**
   * Super constructor for derived classes.
   * @param rvOperation The cumulative operation that will be applied.
   */
  DiscreteProbability(CumulativeOperation rvOperation) {
    this.rvOperation =
        Objects.requireNonNull(rvOperation,"rvOperation can't be null.");
  }

  /**
   * Computes the cumulative distribution function of the
   * probability, given the random variable.
   * <p>
   * This does not need to be
   * overridden unless the cumulative operation can be
   * computed by means other than summing the probability
   * distribution function. Override {@link #computeResult} to
   * provide the PDF that this function uses.
   *
   * @param randomVariable The random variable of the
   *                       probability function
   * @return The cumulative probability result.
   */
   double getResult(int randomVariable) {
    return this.getCumulativeOperation()
               .apply(randomVariable, this::computeResult);
  }

  /**
   * The CumulativeOperation that will be
   * applied should the getResult method be called.
   *
   * @return The cumulative operation used to compute
   * this probability.
   */
  public CumulativeOperation getCumulativeOperation() {
    return rvOperation;
  }

  /**
   * @return  Returns the mean or expected value.
   */
  public abstract double getExpectedValue();

  /**
   *
   * @return Returns the variance.
   */
  public abstract double getVariance();

  /**
   * Returns the reciprocal.
   */
  public static double reciprocal(double value) {
    return 1.0d / value;
  }

  /**
   * product of two doubles.
   */
  public static double product(double left, double right) {
    return left * right;
  }

  /**
   * greatest common divisor
   */
  public static long gcd(long a, long b) {
    while (b > 0) {
      long c = a % b;
      a = b;
      b = c;
    }
    return a;
  }
}
