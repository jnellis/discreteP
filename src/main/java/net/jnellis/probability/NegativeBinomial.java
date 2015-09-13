/*
 * NegativeBinomial.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * The <a href="https://en.wikipedia.org/wiki/Negative_binomial_distribution">
 * Negative Binomial probability distribution.</a>
 * <p>
 * The Negative Binomial Probability is similar to the
 * Geometric Probability in concept but similar to the
 * Binomial Probability in computation.  The Negative
 * Binomial probability answers the question, "What is
 * the chance that the Kth successful trial happens on
 * the Yth trial."
 * or thought about another way, "What is the chance that there are X failures
 * by the Kth success." Where X = Y - K trials.
 * <p>
 * For example, Die rolling, what is the chance of rolling
 * a one EXACTLY three times with the last one happening
 * on the sixth roll. Here K=3, Y=6 and the chance to roll
 * a one is 1/6
 * <p>
 * <pre>
 * double result = NegativeBinomial.probability(3, 1.0/6, 6);
 * </pre>
 * <p>
 * Maybe we want to ask the question of what is the chance of rolling
 * three ones in six or less attempts.
 * <pre>
 * CumulativeOperation.lessThanOrEqual
 *                    .apply(6, y-> NegativeBinomial.probability(3, 1.0/6, y));
 * </pre>
 */
public class NegativeBinomial extends DiscreteProbability {

  final int successfulTrials;
  final double chanceOfSuccess;

  /*	The Negative Binomial Probability  */
  public NegativeBinomial(CumulativeOperation rvOperation,
                          int successfulTrials,
                          double chanceOfSuccess) {
    super(rvOperation);

    this.successfulTrials = successfulTrials;
    this.chanceOfSuccess = chanceOfSuccess;
  }


  @Override
  public double computeResult(int randomVariable) {
    return probability(successfulTrials, chanceOfSuccess, randomVariable);
  }


  /*	Constructor Parameters:
      y - number of the trial that the kth success happens; must be 0 <= Y
      k - number of successful trials; must be 0 <= K <= Y
      p - chance of success for each trial; must be 0.0 <= p <= 1.0
      rvOperation - Random variable comparison. Whether this probability is
      cumulative and which
      way it is.
  */

  /**
   * Computes the Negative binomial probability.
   *
   * @param k number of successful trials
   * @param p chance of a successful trial
   * @param y total number of trials
   * @return probability of this event
   */
  public static double probability(int k, double p, int y) {
    /*  The equation for this probability is based on the equation that counts
     *  failures instead of total trials and successes.
     *  P(x) = (x + k - 1)C(k - 1) * p^k * (1-p)^(x)
     *  but when x = y-k
     *  P(y) = ((y-k)+k-1)C(k - 1) * p^k * (1-p)^(y-k)
     *       = (y-1)C(k-1) * p^k * (1-p)^(y-k)
     *       = (y-1)!/(((y-1)-(k-1))!(k-1)!) * p^k * (1-p)^(y-k)
     */
    // validate
    assert DiscreteProbability.nonNegative(k)
        : "Number of successful trials must non-negative";
    assert DiscreteProbability.betweenZeroAndOneInclusive(p)
        : "Chance of a successful trial must be between zero and one.";
    // the base class function GetResult will possibly
    // set our number of trials below K so just return 0.0;
    if (k > y || y == 0)
      return 0.0;


    // initialize some variables
    double result = 1.0;
    double q = 1.0 - p;
    // check optimizations
    if (k == y) {
      return result = Math.pow(p, k);
    }
    if (k == 1) {
      return result = Math.pow(q, y);
    }

    // cancellation optimization, the higher denominator term cancels out.
    // (y-1)!/(((y-1)-(k-1))!(k-1)!)
    final int range = Integer.min((y - 1) - (k - 1), (k - 1));
    int np = k;
    int nq = y - k;
    int denoms = range;
    int numers = y - 1;

    while (np > 0 || nq > 0 || denoms > 0 || numers > (y - 1 - range)) {
      // If the result is greater than one we want to divide by 
      // a denominator digit or multiply by percentage p or q.
      // If we are out of numerator digits then finish multiplying
      // with our powers of p or q or dividing by a denom digit.
      if (result >= 1.0 || numers == (y - 1 - range)) {
        if (denoms > 0) {
          //m_resut *= (1.0/ndenom);
          result /= denoms;
          --denoms;
        } else if (nq > 0) {
          result *= q;
          --nq;
        } else if (np > 0) {
          result *= p;
          --np;
        } else {
          throw new IllegalStateException("Binomial Probability computation " +
                                              "error- check success " +
                                              "percentage between 0 and 1");
        }
      }
      // If the result is less than one then we want to multiply
      // by a numerator digit. If we are out of denominator digits,
      // powers of p or powers of q then multiply rest of result 
      // by numerator digits.
      else if (result < 1.0 || np == 0 /* || nq ==0 || ndenom ==0 */) {
        if (numers > (y - 1 - range)) {
          result *= numers;
          --numers;
        } else {
          throw (new IllegalStateException("Binomial Probability computation " +
                                               "error- unknown error"));
        }
      } else {
        throw (new IllegalStateException("Binomial Probability computation " +
                                             "error- possible value " +
                                             "infinity or YaY"));
      }
    }
    return result;
  }

  // The Expected Value is defined by
// E(Y) = K / p
  @Override
  public double getExpectedValue() {
    return successfulTrials / chanceOfSuccess;
  }

  // The variance is defined by
// o^2 = K*(1-p)/p^2
  @Override
  public double getVariance() {
    return successfulTrials * (1 - chanceOfSuccess) / (chanceOfSuccess *
        chanceOfSuccess);
  }
}
