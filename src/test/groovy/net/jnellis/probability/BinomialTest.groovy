/*
 * BinomialTest.groovy
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability

import spock.lang.Specification
import spock.lang.Unroll

import static net.jnellis.probability.CumulativeOperation.*

/**
 * User: Joe Nellis
 * Date: 8/14/2015 
 * Time: 7:37 PM
 *
 */
class BinomialTest extends Specification {

  double resolution = 1.0E-12

  def "test simple equals probability"() {
    setup:
    def trials = 8
    def chanceOfSuccess = 0.5

    expect:
    Math.abs(
        new Binomial(equal, trials, chanceOfSuccess).getResult(rv) - result
    ) < resolution

    where:
    rv | result
    0  | 0.00390625
    1  | 0.03125
    2  | 0.109375
    3  | 0.21875
    4  | 0.2734375
    5  | 0.21875
    6  | 0.109375
    7  | 0.03125
    8  | 0.00390625
  }

  @Unroll
  def "test #N trials of #p success each with P(Y=#rv) = #result"() {

    expect:
    Math.abs(Binomial.probability(N, p, rv) - result) < resolution

    where:
    N   | p           | rv  | result
    1   | 0.0         | 0   | 1d
    1   | 0.0         | 1   | 0
    1   | 1.0d        | 0   | 0
    1   | 1.0d        | 1   | 1d
    2   | 1.0d        | 0   | 0
    2   | 1.0d        | 1   | 0
    2   | 1.0d        | 2   | 1d
    17  | 0.5d        | 7   | 0.14837646484375d
    67  | 0.99d       | 67  | 0.509985746249565d
    67  | 0.01d       | 67  | 0
    600 | 1.0d / 3.0d | 200 | 0.0345326241871131d
  }

  @Unroll
  def "test #N trials of #p success each with P(Y<#rv) = #result"() {
    expect:
    double probability = lessThan.apply(rv) { Binomial.probability(N, p, it) }

    Math.abs(probability - result) < resolution

    where:
    N   | p           | rv  | result
    1   | 0.0         | 0   | 0
    1   | 0.0         | 1   | 1
    1   | 1.0         | 0   | 0
    1   | 1.0         | 1   | 0
    2   | 1.0         | 0   | 0
    2   | 1.0         | 1   | 0
    2   | 1.0         | 2   | 0
    17  | 0.5         | 7   | 0.166152954101563
    67  | 0.99        | 67  | 0.490014253750435
    67  | 0.01        | 67  | 1
    600 | 1.0d / 3.0d | 200 | 0.484649326421801
  }


  def "test cumulative operation to itself"() {
    setup:
    def trials = 8
    def chanceOfSuccess = 0.5

    expect:
    Math.abs(
        new Binomial(lessThanOrEqual, trials, chanceOfSuccess).getResult(rv)
            - (1.0 - new Binomial(greaterThan, trials, chanceOfSuccess)
            .getResult(rv))
    ) < resolution


    Math.abs(
        new Binomial(greaterThanOrEqual, trials, chanceOfSuccess).getResult(rv)
            - (1.0 - new Binomial(lessThan, trials, chanceOfSuccess)
            .getResult(rv))
    ) < resolution

    Math.abs(
        new Binomial(equal, trials, chanceOfSuccess).getResult(rv)
            - (1.0 - new Binomial(notEqual, trials, chanceOfSuccess)
            .getResult(rv))
    ) < resolution
    where:
    rv << (0..8)
  }

  def "test cumulative probability with high number of trials"() {
    setup:
    def trials = 8009
    def chanceOfSuccess = 0.5
    def result = new Binomial(lessThanOrEqual, trials, chanceOfSuccess).getResult(rv)

    expect:
    Math.abs(result - 0.473273430936) < resolution * 1E5
    // 4001 adds of small decimal numbers loses resolution
    where:
    rv << 4001

  }

  def "test exact probability with high number of trials"() {
    setup:
    def trials = 80009
    def chanceOfSuccess = 0.5
    def result = Binomial.probability(trials, chanceOfSuccess, rv);

    expect:
    Math.abs(result - 0.0028199161817) < resolution * 1E3
    // Here the resolution loss is from 5 digits in N.
    where:
    rv << 40001

  }

  def "test cumulative probabilty with medium high number of trials"() {
    setup:
    def trials = 997
    def chanceOfSuccess = 0.55

    expect:
    double result = new Binomial(lessThanOrEqual, trials, chanceOfSuccess).getResult(rv)
    Math.abs(result - 0.00219083926527885) < resolution
    where:
    rv << 503

  }

}
