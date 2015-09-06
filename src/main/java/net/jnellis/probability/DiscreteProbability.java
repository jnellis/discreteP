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
   * Returns true if number is greater than or equal to zero
   * @param number value to check
   * @return true if non-negative.
   */
  public static boolean nonNegative(int number) {
    return number >= 0;
  }

  /**
   * @param probability  a probability value
   * @return true if  0 &lt;= probability &lt;= 1.0
   */
  public static boolean betweenZeroAndOneInclusive(double probability) {
    return probability >= 0 && probability <= 1.0;
  }

  /**
   * Returns the reciprocal.
   * @param value   a denominator
   * @return 1.0 / value
   */
  public static double reciprocal(double value) {
    return 1.0d / value;
  }

  /**
   * product of two doubles without overflow checks.
   * @param left   identity
   * @param right  nextValue
   * @return left * right
   */
  public static double product(double left, double right) {
    return left * right;
  }

  /**
   * greatest common divisor
   *
   * @param a an integer
   * @param b an integer
   * @return value that divides evenly into both 'a' and 'b'
   */
  public static long gcd(long a, long b) {
    while (b > 0) {
      long c = a % b;
      a = b;
      b = c;
    }
    return a;
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
  public double getResult(int randomVariable) {
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
   * @return The expected value, or mean, for this probability distribution.
   */
  public abstract double getExpectedValue();

  /**
   *
   * @return The variance (?2) of the probability distribution.
   */
  public abstract double getVariance();
}
