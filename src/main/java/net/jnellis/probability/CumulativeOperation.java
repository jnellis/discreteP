/*
 * CumulativeOperation.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

import java.util.function.IntToDoubleFunction;
import java.util.stream.IntStream;

/**
 * A functional interface that represents a cumulative operation with a probability
 * function and the range of a random variable. The cumulative result is
 * achieved by the summing the probability function result from zero to random
 * variable number of times. The cumulative operation is
 * optimized to only compute the lower range from zero to the random variable.
 * <i>greaterThan</i> and <i>greaterThanOrEqual</i> are derived from subtracting
 * this sum from one.
 */
@FunctionalInterface
public interface CumulativeOperation {

  /**
   * The cumulative operation
   *
   * @param probabilityFunction A probability function that returns from 0 to 1.0
   * @param randomVariable      The P(Y= ?) random variable of the probability.
   * @return A cumulative probability in the range of 0 to 1.0
   */
  double apply(IntToDoubleFunction probabilityFunction, int randomVariable);

  /**
   * Applies only the random variable to the probabilityFunction once.
   */
  CumulativeOperation equal = IntToDoubleFunction::applyAsDouble;

  /**
   * Computes the sum of the probabilities of the random variable from
   * zero up to the random variable.
   */
  CumulativeOperation lessThan =
      (p, rv) -> IntStream.range(0, rv)
                          .parallel()
                          .mapToDouble(p::applyAsDouble)
                          .sum();

  /**
   * Computes the sum of the probabilities of the random variable from
   * zero up to and including the random variable.
   */
  CumulativeOperation lessThanOrEqual =
      (p, rv) -> IntStream.rangeClosed(0, rv)
                          .parallel()
                          .mapToDouble(p::applyAsDouble)
                          .sum();

  /**
   * Computes the 1.0 - result of the <i>equal</i> cumulative
   * operation.
   */
  CumulativeOperation notEqual =
      (p, rv) -> 1.0 - CumulativeOperation.equal.apply(p, rv);

  /**
   * Computes the 1.0 - result of the lessThanOrEqual cumulative operation.
   */
  CumulativeOperation greaterThan =
      (p, rv) -> 1.0 - CumulativeOperation.lessThanOrEqual.apply(p, rv);

  /**
   * Computes the 1.0 - result of the lessThan cumulative operation.
   */
  CumulativeOperation greaterThanOrEqual =
      (p, rv) -> 1.0 - CumulativeOperation.lessThan.apply(p, rv);

}
