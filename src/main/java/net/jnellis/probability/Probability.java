/*
 * Probability.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * Represents probability distribution function.
 */
public interface Probability {

  /**
   * The
   * @return
   */
  CumulativeOperation getCumulativeOperation();

  default double getResult(int randomVariable) {
    return this.getCumulativeOperation()
               .apply(this::computeResult, randomVariable);
  }

  double computeResult(int randomVariable);

  static double reciprocal(int value) {
    return 1.0d / value;
  }

  static double reciprocal(double value) {
    return 1.0d / value;
  }

  static double product(double left, double right) {
    return left * right;
  }

  static long gcd(long a, long b) {
    while (b > 0) {
      long c = a % b;
      a = b;
      b = c;
    }
    return a;
  }



}

