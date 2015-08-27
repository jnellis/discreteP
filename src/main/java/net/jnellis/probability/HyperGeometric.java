package net.jnellis.probability;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * User: Joe Nellis
 * Date: 8/24/2015
 * Time: 1:32 PM
 */
public class HyperGeometric extends DiscreteProbability {

  final int populationSize;
  final int sampleSize;
  final int successStates;

  /**
   * The Hypergeometric probability distribution.
   *
   * @param rvOperation
   * @param populationSize Population size
   * @param sampleSize     Size of sample drawn from population.
   * @param successStates  Number of Success items in the population.
   */
  public HyperGeometric(net.jnellis.probability.CumulativeOperation rvOperation,
                        int populationSize,
                        int sampleSize,
                        int successStates) {
    super(rvOperation);
    this.populationSize = populationSize;
    this.sampleSize = sampleSize;
    this.successStates = successStates;
  }

  // The equation: (rCy)*((N-r)C(n-y))/(NCn)
  // r!/(r-y)!y!  *   (N-r)!/(N-r-(n-y))!(n-y)!  /  N!/(N-n)!n!
/* Computing the result
  The computation is composed of pure combinations which are
	handled in the same fashion as the Binomial and NegativeBinomial
	probabilities.  The equation is broken up into numerators and
	denominators and then appropriately multiplied by or divided by
	depending on the running result being above or below 1.0.
*/
  @Override
  public double computeResult(int randomVariable) {
    return probability(populationSize, sampleSize, successStates, randomVariable);
  }

  public static double probability(int N, int n, int r, int y) {
    assert (y <= r);
    assert (n - y <= N - r);
    assert (y >= 0);
    assert (N > 0);
    assert (r >= 0);
    assert (n >= 0);
    if (N == 0)
      return 0.0;
    double result = 1.0;

    int numer1, numer2, numer3, numer1cmp, numer2cmp, numer3cmp;
    int denom1, denom2, denom3, range1, range2, range3;
    numer1 = numer1cmp = r;
    numer2 = numer2cmp = N - r;
    numer3 = numer3cmp = N;

    //optimization on (rCy)
    range1 = denom1 = Integer.min(r - y, y);

    //optimization on ((N-r)C(n-y))
    range2 = denom2 = Integer.min((N - r) - (n - y), n - y);

    //optimization on (NCn)
    range3 = denom3 = Integer.min(N - n, n);

    while (numer3 > numer3cmp - range3 || denom3 > 1) {
      if (result > 1.0 || denom3 == 1) {
        if (denom1 > 1)
          result = result / denom1--;
        else if (denom2 > 1)
          result = result / denom2--;
        else if (numer3 > numer3cmp - range3)
          result = result / numer3--;
        else
          throw new IllegalStateException("HyperGeometric Probability error- premature end of " +
                                              "divisors");
      } else {
        if (numer1 > numer1cmp - range1)
          result = result * numer1--;
        else if (numer2 > numer2cmp - range2)
          result = result * numer2--;
        else if (denom3 > 1)
          result = result * denom3--;
        else
          throw new IllegalStateException("HyperGeometric Probability error- premature end of " +
                                              "multipliers");
      }
    }
    return result;
  }

  static int getFirstGreaterThanOneThenDecrement(int[] factors) {
    int idx = IntStream.range(0, factors.length)
                       .filter(i -> factors[i] > 1)
                       .findFirst()
                       .getAsInt();
    return factors[idx]--;
  }

  static boolean allEqualOne(int[] values) {
    return Arrays.stream(values).allMatch((val) -> val == 1);
  }


  @Override
  double getExpectedValue() {
    return 0;
  }

  @Override
  double getVariance() {
    return 0;
  }

}
