package net.jnellis.probability;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * User: Joe Nellis
 * Date: 8/9/2015
 * Time: 8:37 PM
 */
public class Binomial extends DiscreteProbability {

  private final int trials;
  private final double chanceOfSuccess;

  public Binomial(net.jnellis.probability.CumulativeOperation rvOperation,
                  int trials,
                  double chanceOfSuccess) {
    super(rvOperation);

    assert nonNegative(trials);
    assert betweenZeroAndOneInclusive(chanceOfSuccess);

    this.trials = trials;
    this.chanceOfSuccess = chanceOfSuccess;
  }

  public final boolean nonNegative(int number) {
    return number >= 0;
  }

  public final boolean betweenZeroAndOneInclusive(double probability) {
    return probability >= 0 && probability <= 1.0;
  }

  /**
   * The discrete binomial probability.
   * The equation for this probability is:
   * P(Y) = n! /((n-y)! * y!) * p^(y) * q^(n-y)
   * where n is the number of trials,
   * y is the number of successful trials, aka randomVariable,
   * p is the chance of a trial succeeding,
   * q is the chance of a trial failing, (1 - p).
   */
  @Override
  public double computeResult(int randomVariable) {
    // no trials, negative successes, or more successes than possible,
    // has zero probability anything happened.
    if (trials == 0 || randomVariable < 0 || randomVariable > trials) {
      return 0.0;
    }

    // initialize some variables
    int n = trials;
    int y = randomVariable;
    double p = chanceOfSuccess;
    double q = 1.0 - chanceOfSuccess;

    // The factorial and P components cancel out leaving the Q component.
    if (y == 0) {
      return Math.pow(q, n);
    }
    // The factorial and Q components cancel out leaving the P component.
    if (y == n) {
      return Math.pow(p, y);
    }

    // In the factorial part of the equation [ n!/((n-y)!y!) ],
    // we can easily cancel out (n-y)! or y! but not both so
    // choose the larger of the two denominators to cancel out and
    // keep the smaller denominator component.
    int denominatorRange = Integer.min(n - y, y);

    // the (n - denominatorRange) to n components of the numerator factorial
    DoubleStream numerators = IntStream.rangeClosed(n - denominatorRange + 1, n)
                                       .asDoubleStream();

    // counters of p, q, numerators, and denominators
    int pees = y;
    int ques = n - y;
    int denom = denominatorRange;
    double result = 1.0;

    // count down the numerators from highest to lowest
    for (int numer = n; numer > (n - denominatorRange); numer--) {
      result *= numer;
      while (result >= 1.0) {
        if (denom > 1) {
          result = result / denom;
          denom--;
        } else if (ques > 0) {
          result *= q;
          ques--;
        } else if (pees > 0) {
          result *= p;
          pees--;
        } else {
          throw new IllegalStateException("unexpected end of small values");
        }
      }
    }
    // multiply by remaining p's, q's, and denominators.
    result *= Math.pow(p, pees);
    result *= Math.pow(q, ques);
    while (denom > 1) {
      result /= denom;
      denom--;
    }

    return result;
  }


  // The Expected Value or population mean is defined
  // by E(V) = N*p
  @Override
  public double getExpectedValue() {
    return trials * chanceOfSuccess;
  }

  // the Variance is defined by
  // o^2 = N*p*q
  @Override
  public double getVariance() {
    return trials * chanceOfSuccess * (1.0 - chanceOfSuccess);
  }

}
