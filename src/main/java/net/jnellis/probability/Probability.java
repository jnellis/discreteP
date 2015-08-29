/*
 * Probability.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * Represents a probability distribution function
 */
@FunctionalInterface
public interface Probability {

  /**
   * Computes the probability distribution function given the
   * the random variable.
   *
   * @param randomVariable The random variable of the
   *                       probability function
   * @return The probability between zero and one inclusive.
   */
  double computeResult(int randomVariable);


}

