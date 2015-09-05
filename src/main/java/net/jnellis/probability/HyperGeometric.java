/*
 * HyperGeometric.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * The <a href="https://en.wikipedia.org/wiki/Hypergeometric_distribution">
 * Hypergeometric probability distribution.</a>
 * <p>
 * This probability is the one used to compute odds in
 * many of the lotteries in the United States.  It is used when
 * you are picking from two sets in a mixed population. The
 * basic example is a bag filled with red and black chips. 5 red
 * chips and 4 black ones.  You are asked to choose 3 chips
 * without looking.  What is the chance you will pick exactly
 * two black chips.  If you look below you will see the construction
 * parameters.  The population size, N, is 4 + 5 = 9. The sample size,
 * n, is 3 since we are picking 3 chips.  The sample set we are interested
 * in is black chips which there are 3 of so r = 3.  And finally the
 * random variable, y, is 2, the number of black chips we want to choose out of
 * 3 pulls.  Chips are not replaced between pulls.
 * </p>
 * <code>
 * double probability = HyperGeometric.probability(9, 3, 3, 2)
 * </code>
 * <p>
 * To compute lottery odds lets assume the lottery in question has
 * 50 numbers and they choose 6 of them at random live on television.
 * So we know N = 50 and the sample size n=6. You have 6 numbers you paid
 * money for that you are interested in, these six numbers represent the
 * selected set r, r = 6.  Now for the grand payoff, getting all six numbers
 * you need all picked numbers to be in your selected set.  y = 6.  Often there
 * is a lesser payout for matching less than six numbers.
 * Finally you usually win your money back if you match two numbers so here
 * y = 2.  To find the chance of winning anything you would compute
 * P(y &gt;= 2) since usually matching one number has no payout and zero matches
 * is definitely not paying out.
 * </p>
 * <pre>
 * // Chance to win anything is when P(y &gt; 2)
 * HyperGeometric cumulative = new HyperGeometric(greaterThanOrEqual,50,6,6)
 * double result = cumulative.getResult(2);
 * // it's a 14.572% chance to win anything back
 *
 * // Here's a static approach that doesn't create a HyperGeometric object.
 * CumulativeOperation.greaterThanOrEqual
 *                    .apply(rv-&gt; HyperGeometric.probability(50,6,6, rv), 2);
 *
 * </pre>
 */
public class HyperGeometric extends DiscreteProbability {

  private final int N;
  private final int n;
  private final int r;

  /**
   * The Hypergeometric probability distribution.
   *
   * @param rvOperation    CumulativeOperation to apply
   * @param populationSize Population size
   * @param sampleSize     Size of sample drawn from population.
   * @param successStates  Number of Success items in the population.
   */
  public HyperGeometric(CumulativeOperation rvOperation,
                        int populationSize,
                        int sampleSize,
                        int successStates) {
    super(rvOperation);
    this.N = populationSize;
    this.n = sampleSize;
    this.r = successStates;
  }

  /* Computing the result:
    The computation is composed of pure combinations which are
    handled in the same fashion as the Binomial and NegativeBinomial
    probabilities.  The equation is broken up into numerators and
    denominators and then appropriately multiplied by or divided by
    depending on the running result being above or below 1.0.
  */
  @Override
  public double computeResult(int randomVariable) {
    return probability(N, n, r, randomVariable);
  }

  /**
   * Computes the HyperGeometric probability of a random variable.
   * <pre>
   *          (rCy) * ((N-r)C(n-y))
   * which is --------------------- where xCy is the combination 'x choose y'
   *                 (NCn)
   *
   *              x!
   * xCy = -------------
   *         (x-y)! * y!
   * </pre>
   *
   * @param N Population size
   * @param n sample size
   * @param r number of success states in population.
   * @param y the number of success states we're interested in.
   * @return The probability of this event, between 0.0 and 1.0 inclusive.
   */
  public static double probability(int N, int n, int r, int y) {
    assert (y >= 0) : "Random variable must be greater than or equal to zero.";
    assert (N > 0) : "Population size must be greater than zero.";
    assert (r >= 0) : "Success states must be greater than or equal to zero.";
    assert (n >= 0) : "Sample size must be greater than or equal to zero.";
    assert (y <= r) : "Expected success must be <= possible success states.";
    // The number of failure states (n-y) must be less than or equal
    // to the number of total failure states possible (N-r).
    if (!(n - y <= N - r)) {
      return 0.0;  // otherwise 0% chance this event happens.
    }
    //break equation up into ranges of numerators and denominators
    int numer1 = r;
    int numer2 = N - r;
    int numer3 = N;

    // The equation: (rCy)*((N-r)C(n-y))/(NCn)
    // r!/(r-y)!y!  *   (N-r)!/(N-r-(n-y))!(n-y)!  /  N!/(N-n)!n!
    //cancellation optimization on (rCy)
    final int range1 = Integer.min(r - y, y);
    //cancellation optimization on ((N-r)C(n-y))
    final int range2 = Integer.min((N - r) - (n - y), n - y);
    //cancellation optimization on (NCn)
    final int range3 = Integer.min(N - n, n);

    final int numer1floor = numer1 - range1;
    final int numer2floor = numer2 - range2;
    final int numer3floor = numer3 - range3;

    int denom1 = range1;
    int denom2 = range2;
    int denom3 = range3;

    // r!/(r-y)!y!  *   (N-r)!/(N-r-(n-y))!(n-y)!  * (N-n)!n!/N!
    // swap numerator and denominator on last component, N!/((N-n)!n!)
    double result = 1.0;
    while (numer3 > numer3floor || denom3 > 0) {
      if (result > 1.0 || denom3 == 0) {
        if (denom1 > 0)
          result = result / denom1--;
        else if (denom2 > 0)
          result = result / denom2--;
        else
          result = result / numer3--;
      } else {
        if (numer1 > numer1floor)
          result = result * numer1--;
        else if (numer2 > numer2floor)
          result = result * numer2--;
        else
          result = result * denom3--;
      }
    }
    return result;
  }

  /**
   * The expected value or population mean is defined by:
   * E(Y) = sampleSize*#successStates / populationSize
   *
   * @return The expected value for this probability distribution.
   */
  @Override
  double getExpectedValue() {
    return 1.0 * n * r / N;
  }

  /**
   * The variance is defined by:
   * V(Y) = n * (r/N) * (N-r)/N * (N-n)/(N-1)
   * Where N is the population size,
   * n is the sample size,
   * r is the number of success states
   *
   * @return The variance (?2) of the probability distribution.
   */
  @Override
  double getVariance() {
    if (N == 1)
      return 0.0;
    return 1.0 * n * r / N * (N - r) / N * (N - n) / (N - 1);
  }

}
