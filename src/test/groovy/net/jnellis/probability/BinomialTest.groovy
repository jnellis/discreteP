package net.jnellis.probability

import spock.lang.Specification

import static net.jnellis.probability.CumulativeOperation.*

/**
 * User: Joe Nellis
 * Date: 8/14/2015 
 * Time: 7:37 PM
 *
 */
class BinomialTest extends Specification {

  def binomial
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

  def "test cumulative probabilty"() {
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
    def result =
        new Binomial(lessThanOrEqual, trials, chanceOfSuccess).getResult(rv)
    println result
    println 0.473273430936

    expect:
    Math.abs(
        result - 0.473273430936
    ) < resolution * 1E5 // 4001 adds of small decimal numbers loses resolution
    where:
    rv << 4001

  }

  def "test exact probability with high number of trials"() {
    setup:
    def trials = 80009
    def chanceOfSuccess = 0.5
    def result = new Binomial(equal, trials, chanceOfSuccess).getResult(rv)
    println result
    println 0.0028199161817
    expect:
    Math.abs(
        result - 0.0028199161817
    ) < resolution * 1E3 // Here the resolution loss is from 5 digits in N.
    where:
    rv << 40001

  }

  def "test cumulative probabilty with medium number of trials"() {
    setup:
    def trials = 997
    def chanceOfSuccess = 0.55

    expect:
    Math.abs(
        new Binomial(lessThanOrEqual, trials, chanceOfSuccess).getResult(rv)
            - 0.00219083926527885
    ) < resolution
    where:
    rv << 503

  }

}
