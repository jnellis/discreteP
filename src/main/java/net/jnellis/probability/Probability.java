package net.jnellis.probability;

/**
 * User: Joe Nellis
 * Date: 8/2/2015
 * Time: 6:37 PM
 */
public interface Probability {

  @FunctionalInterface
  interface CumulativeOperation
      extends net.jnellis.probability.CumulativeOperation {

  }

  net.jnellis.probability.CumulativeOperation getCumulativeRandomVariable();

  default double getResult(int randomVariable) {
    return this.getCumulativeRandomVariable()
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
