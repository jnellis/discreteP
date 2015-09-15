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
 * <pre>
 * double result = NegativeBinomial.probability(3, 1.0/6, 6);
 * </pre>
 * <p>
 * Maybe we want to ask the question of what is the chance of rolling
 * three ones in six or less attempts.
 * <pre>
 * CumulativeOperation.lessThanOrEqual
 *                    .apply(6, y-&gt; NegativeBinomial.probability(3, 1.0/6,
 *                    y));
 * </pre>
 */
public class NegativeBinomial extends DiscreteProbability {

  private final int successfulTrials;
  private final double chanceOfSuccess;

  /**
   * Creates a representation of the Negative binomial distribution.
   *
   * @param rvOperation      The total number of trials, the random variable.
   * @param successfulTrials expected number of successful trials
   * @param chanceOfSuccess  chance of success for a single trial.
   */
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

  /**
   * Computes the Negative binomial probability.
   *
   * @param successfulTrials number of successful trials
   * @param chanceOfSuccess  chance of a successful trial
   * @param totalTrials      total number of trials
   * @return probability of this event
   */
  public static double probability(int successfulTrials,
                                   double chanceOfSuccess,
                                   int totalTrials) {
    /*  The equation for this probability is based on the equation that counts
     *  failures instead of total trials and successes.
     *  where x is failed trials,
     *        k is successful trials
     *        p is chance of success
     *        y is total trials
     *  P(x) = (x + k - 1)C(k - 1) * p^k * (1-p)^(x)
     *  but when x = y-k
     *  P(y) = ((y-k)+k-1)C(k - 1) * p^k * (1-p)^(y-k)
     *       = (y-1)C(k-1) * p^k * (1-p)^(y-k)
     *       = (y-1)!/(((y-1)-(k-1))!(k-1)!) * p^k * (1-p)^(y-k)
     */
    // validate
    assert DiscreteProbability.nonNegative(successfulTrials)
        : "Number of successful trials must non-negative";
    assert DiscreteProbability.betweenZeroAndOneInclusive(chanceOfSuccess)
        : "Chance of a successful trial must be between zero and one.";
    // the base class function GetResult will possibly
    // set our number of trials below K so just return 0.0;
    if (successfulTrials > totalTrials || totalTrials == 0)
      return 0.0;

    // initialize some variables
    double result = 1.0;
    double chanceOfFailure = 1.0 - chanceOfSuccess;
    // check optimizations
    if (successfulTrials == totalTrials) {
      return Math.pow(chanceOfSuccess, successfulTrials);
    }
    if (successfulTrials == 1) {
      return Math.pow(chanceOfFailure, totalTrials);
    }

    // cancellation optimization, the larger denominator term cancels out.
    // (y-1)!/(((y-1)-(k-1))!(k-1)!)
    final int range = Integer.min((totalTrials - 1) - (successfulTrials - 1),
                                  (successfulTrials - 1));
    int pees = successfulTrials;
    int ques = totalTrials - successfulTrials;
    int denoms = range;
    int numers = totalTrials - 1;
    int numerFloor = totalTrials - 1 - range;

    while (pees > 0 || ques > 0 || denoms > 0 || numers > numerFloor) {
      if (result >= 1.0 || numers == numerFloor) {
        if (denoms > 0) {
          result /= denoms;
          --denoms;
        } else if (ques > 0) {
          result *= chanceOfFailure;
          --ques;
        } else {
          result *= chanceOfSuccess;
          --pees;
        }
      } else {
        result *= numers;
        --numers;
      }
    }
    return result;
  }

  /**
   * The Expected Value is defined by
   * E(Y) = successfulTrials / chanceOfSuccess
   */
  @Override
  public double getExpectedValue() {
    return successfulTrials / chanceOfSuccess;
  }

  /**
   * The variance is defined by
   * o^2 = k * (1-p)/p^2
   * where k is the # successful trials,
   * p is chance of success
   */
  @Override
  public double getVariance() {
    return successfulTrials * (1 - chanceOfSuccess) / (chanceOfSuccess *
        chanceOfSuccess);
  }
}
