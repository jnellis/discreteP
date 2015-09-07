/*
 * Geometric.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * The <a href="https://en.wikipedia.org/wiki/Geometric_distribution">
 * Geometric probability distribution.</a>
 * <p>
 * This uses the first distribution noted in the above linked article
 * regarding which geometric distribution is used. This probability distribution
 * is of the number of trials needed to get one success rather than the
 * number of failures before the first success, which is a special case of the
 * negative binomial distribution when the number of successes is one.
 * <p>
 * The geometric probability asks the question, "Given the chance
 * that each trial being successful is the same, what is the probability
 * that the first success happens on a the Yth trial."
 * This can be referred to as a russian roulette type of question.  If a
 * revolver holds one bullet out of six chambers, the chance for each
 * trial is the same.  The probability answers the percentage that bullet
 * will go off in each successive round given all the previous rounds
 * were unsuccessful. This can be seen as the simple multiplication rule of
 * probability, for example, P(Y=3) = chance of failure * chance of failure *
 * chance of success = 5/6 * 5/6 * 1/6 = 25/216
 *
 * <pre>
 * double result = Geometric.probability(3,1.0/6.0);
 * </pre>
 */
public class Geometric extends DiscreteProbability {

  private final double p;

  public Geometric(CumulativeOperation rvOperation,
                   double chanceOfSuccess) {
    super(rvOperation);
    this.p = chanceOfSuccess;
  }

  /**
   * Expected value or population mean is defined by
   * E(Y) = 1/p
   */
  @Override
  public double getExpectedValue() {
    return p == 0.0 ? Double.POSITIVE_INFINITY : reciprocal(p);
  }

  /**
   * Variance is defined by
   * V(Y)= (1-p)/p^2
   */
  @Override
  public double getVariance() {
    return p == 0.0 ? Double.POSITIVE_INFINITY : (1.0 - p) / (p * p);
  }

  @Override
  public double computeResult(int randomVariable) {
    return probability(p, randomVariable);
  }

  /**
   * Computes the geometric distribution probability.
   *
   * @param chanceOfSuccess probability the event succeeds
   * @param onTrial         the number of the trial that the event succeeds on.
   * @return the chance the event happens on this trial
   */
  public static double probability(double chanceOfSuccess, int onTrial) {
    if (onTrial == 0) {
      return 0;
    }
    return Math.pow((1.0 - chanceOfSuccess), onTrial - 1) * chanceOfSuccess;
  }
}
