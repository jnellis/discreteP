/*
 * Probability.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * Represents a probability distribution function and a cumulative
 * operation.
 */
public interface Probability {

  /**
   * The getter for the CumulativeOperation that will be
   * applied should the getResult method be called.
   *
   * @return The cumulative operation used to compute
   * this probability.
   */
  CumulativeOperation getCumulativeOperation();

  /**
   * Computes the cumulative distribution function of the
   * probability, given the random variable.
   * <p>
   * This is the default method and does not need to be
   * overridden unless the cumulative operation can be
   * computed by means other than summing the probability
   * distribution function. Override {@link #computeResult} to
   * provide the PDF that this function uses.
   *
   * @param randomVariable The random variable of the
   *                       probability function
   * @return The cumulative probability result.
   */
  default double getResult(int randomVariable) {
    return this.getCumulativeOperation()
               .apply(this::computeResult, randomVariable);
  }

  /**
   * Computes the probability distribution function given the
   * the random variable.
   *
   * @param randomVariable The random variable of the
   *                       probability function
   * @return The probability between zero and one inclusive.
   */
  double computeResult(int randomVariable);


  /**
   * Returns the reciprocal.
   */
  static double reciprocal(double value) {
    return 1.0d / value;
  }

  /**
   * product of two doubles.
   */
  static double product(double left, double right) {
    return left * right;
  }

  /**
   * greatest common divisor
   */
  static long gcd(long a, long b) {
    while (b > 0) {
      long c = a % b;
      a = b;
      b = c;
    }
    return a;
  }


}

