package net.jnellis.probability;

import java.util.PrimitiveIterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: Joe Nellis
 * Date: 8/24/2015
 * Time: 1:32 PM
 */
public class HyperGeometric extends DiscreteProbability {

  final int N;
  final int n;
  final int r;

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
    return probability(N, n, r, randomVariable);
  }

  public static double probability(int N, int n, int r, int y) {
    if (N == 0)
      return 0.0;
    assert (y <= r);
    assert (n - y <= N - r);
    assert (y >= 0);
    assert (N > 0);
    assert (r >= 0);
    assert (n >= 0);

    //optimization on (rCy)
    int range1 = Integer.min(r - y, y);
    //optimization on ((N-r)C(n-y))
    int range2 = Integer.min((N - r) - (n - y), n - y);
    //optimization on (NCn)
    int range3 = Integer.min(N - n, n);


    // r!/(r-y)!y!  *   (N-r)!/(N-r-(n-y))!(n-y)!  /  N!/(N-n)!n!
    // swap numerator and denominator on last component, N!/((N-n)!n!)
    // r-range1+1:r   *   (N-r)-range2+1:(N-r)   *   1:range3   // numerators
    PrimitiveIterator.OfDouble numerators =
        Stream.of(IntStream.rangeClosed(r - range1 + 1, r),
                  IntStream
                      .rangeClosed(N - r - range2 + 1, N - r),
                  IntStream.rangeClosed(1, range3))
              .flatMapToDouble(IntStream::asDoubleStream)
              .iterator();

    // 1:range1   *   1:range2    *    N-range3+1 : N        // denominators
    PrimitiveIterator.OfDouble denominators =
        Stream.of(IntStream.rangeClosed(1, range1),
                  IntStream.rangeClosed(1, range2),
                  IntStream.rangeClosed(N - range3 + 1, N))
              .flatMapToDouble(IntStream::asDoubleStream)
              .iterator();

    double result = 1.0;
    while (numerators.hasNext() || denominators.hasNext()) {
      if (result >= 1.0 || !numerators.hasNext()) {
        result /= denominators.nextDouble();
      } else {
        result *= numerators.nextDouble();
      }
    }
    return result;
  }


  // The expected value or population mean is defined by:
// E(Y) = n*r/N
  @Override
  double getExpectedValue() {
    return 1.0 * n * r / N;
  }


  // The variance is defined by:
// V(Y) = n * (r/N) * (N-r)/N * (N-n)/(N-1)
  @Override
  double getVariance() {
    if (N == 1)
      return 0.0;
    return 1.0 * n * r / N * (N - r) / N * (N - n) / (N - 1);
  }

}
