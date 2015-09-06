/*
 * Binomial.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

/**
 * The <a href="https://en.wikipedia.org/wiki/Binomial_distribution">
 * Binomial probability distribution.</a>
 * <p>
 * Use when you need to ask the question,
 * "If the chance of something happening is P percent,
 * what is the probability it will happen Y times out of
 * N attempts."
 * Example: Die rolling. If the chance to roll a one is
 * 1/6 or 16.7%, what is the probability it will happen 3 times
 * out of 5 rolls.
 * <p>
 * Notes: The event must be a discrete event like rolling a die,
 * flipping a coin, or pulling a card.  Events that consider an
 * average percentage that it will happen or if each trial in turn
 * affects the percentage that the event will happen are not
 * modeled by the binomial probability distribution but can give
 * meaningful estimates. ex: A veteran NBA basketball player has a free throw
 * percentage of 70%. What is the probability he makes his next two
 * free throw shots. His first shot would then change his free throw
 * percentage by a very small amount but if this fact is omitted the answer is
 * relatively the same.
 * </p>
 * <pre>
 * // roll 5 dice, what is probability exactly three of them are the same value.
 * double probability = Binomial.probability(5, 0.166666, 3);
 *
 * // roll 5 dice, what is probability 3 or more dice are all the same value.
 * // aka 3-of-a-kind, 4-of-a-kind or 5-of-a-kind.
 * Binomial pdf = new Binomial(greaterThanOrEqual, 5, 0.166666);
 * double result = pdf.getResult(3);
 *
 * // or from a static context that creates no Binomial object
 * result = CumulativeOperation.greaterThanOrEqual
 *                             .apply(3, y -&gt; Binomial.probability(5, 0
 *                             .166666, y));
 * </pre>
 */
public class Binomial extends DiscreteProbability {

  private final int trials;
  private final double chanceOfSuccess;

  /**
   * Creates a representation of a binomial probability distribution.
   *
   * @param rvOperation     The cumulative operation on this probability
   * @param trials          Number of times the experiment takes place
   * @param chanceOfSuccess The probability of the successful event.
   */
  public Binomial(CumulativeOperation rvOperation,
                  int trials,
                  double chanceOfSuccess) {
    super(rvOperation);

    assert nonNegative(trials);
    assert betweenZeroAndOneInclusive(chanceOfSuccess);

    this.trials = trials;
    this.chanceOfSuccess = chanceOfSuccess;
  }

  /**
   */

  @Override
  public double computeResult(int randomVariable) {
    return probability(trials, chanceOfSuccess, randomVariable);
  }

  /**
   * The binomial probability at P(randomVariable)
   * <p>
   * The equation for this probability is:
   * <pre>
   * <code>
   * P(Y) = n! /((n-y)! * y!) * p^(y) * q^(n-y)
   * </code>
   * where n is the number of trials,
   *       p is the chance of a trial succeeding,
   *       q is the chance of a trial failing, (1 - p),
   *       y is the number of successful trials, aka randomVariable,
   * </pre>
   *
   * @param trials          Number of trials of this experiment
   * @param chanceOfSuccess chance each trial succeeds
   * @param randomVariable  number of successes to investigate
   * @return The probability of P(Y = randomVariable)
   */
  public static double probability(final int trials,
                                   final double chanceOfSuccess,
                                   final int randomVariable) {
    // no trials, negative successes, or more successes than possible,
    // has zero probability anything happened.
    if (trials == 0 || randomVariable < 0 || randomVariable > trials) {
      return 0.0;
    }

    final double chanceOfFailure = 1.0 - chanceOfSuccess;

    // Early Optimizations:
    // The factorial and P components cancel out leaving the Q component.
    if (randomVariable == 0) {
      return Math.pow(chanceOfFailure, trials);
    }
    // The factorial and Q components cancel out leaving the P component.
    if (randomVariable == trials) {
      return Math.pow(chanceOfSuccess, randomVariable);
    }

    // In the factorial part of the equation [ n!/((n-y)!y!) ],
    // we can easily cancel out (n-y)! or y! but not both so
    // choose the larger of the two denominators to cancel out and
    // keep the smaller denominator component.
    final int range = Integer.min(trials - randomVariable, randomVariable);
    final int numerFloor = trials - range;

    // counters of p, q, numerator and denominator factors
    int successes = randomVariable;
    int failures = trials - randomVariable;
    int denom = range;
    int numer = trials;

    // While loop soup - computing the result by multiplying by components
    // of the equation in order to maintain an intermediate result that
    // floats around 1.0 if possible.
    double result = 1.0;
    while (successes > 0 || numer > numerFloor) {
      if (result >= 1.0 || numer == numerFloor) { // lowering values
        if (denom > 1) {
          result = result / denom;
          denom--;
        } else if (failures > 0) {
          result *= chanceOfFailure;
          failures--;
        } else {
          result *= chanceOfSuccess;
          successes--;
        }
      } else { //increasing values
        result *= numer;
        numer--;
      }
    }
    return result;
  }

  /**
   * The Expected Value or population mean is defined  by
   * <code>E(V) = trials * chanceOfSuccess</code>
   */
  @Override
  public double getExpectedValue() {
    return trials * chanceOfSuccess;
  }

  /**
   * The Variance is defined by
   * <code>o^2 = trials * chanceOfSuccess * (1-chanceOfSuccess)</code>
   */
  @Override
  public double getVariance() {
    return trials * chanceOfSuccess * (1.0 - chanceOfSuccess);
  }

}
