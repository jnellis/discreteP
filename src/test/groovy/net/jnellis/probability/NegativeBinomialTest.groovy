/*
 * NegativeBinomialTest.groovy
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability

import spock.lang.Specification
import spock.lang.Unroll

import static net.jnellis.probability.CumulativeOperation.lessThan

/**
 * User: Joe Nellis
 * Date: 8/27/2015 
 * Time: 7:36 PM
 *
 */
class NegativeBinomialTest extends Specification {

  double resolution = 1.0e-12

  def "test exact probability"() {
    setup:
    double p = 0.2;
    int k = 3;
    def result = NegativeBinomial.probability(k, p, y)
    expect:
    Math.abs(result - exp) < resolution;

    where:
    y  | exp
    3  | 0.008
    4  | 0.0192
    5  | 0.03072
    10 | 0.0603979776
  }

  @Unroll
  def "test #rv trials with #k k successes of #p success each P(Y=#rv) = #result"() {
    expect:
    double probability = NegativeBinomial.probability(k, p, rv)

    Math.abs(probability - result) < resolution

    where:
    rv  | k   | p           | result
    0   | 0   | 0.5         | 0
    0   | 1   | 0.0         | 0
    0   | 1   | 0.0         | 0
    1   | 0   | 0.0         | 1
    1   | 1   | 0.0         | 0
    1   | 1   | 0.4         | 0.4
    1   | 1   | 1.0         | 1
    1   | 0   | 0.4         | 0.6
    1   | 0   | 1.0         | 0
    2   | 2   | 1.0         | 1
    7   | 7   | 0.5         | 0.0078125
    7   | 1   | 0.5         | 0.0078125
    67  | 67  | 0.99        | 0.509985746249565
    67  | 67  | 0.01        | 0
    97  | 29  | 0.13        | 2.03909902925015E-06
    600 | 200 | 1.0d / 6.0d | 4.16020180421793E-24
  }

  @Unroll
  def "cumulative test #rv trials with #k successes of #p success each; P(Y<#rv) = #result"() {
    expect:
    double probability = lessThan.apply(rv) { NegativeBinomial.probability(k, p, it) }

    Math.abs(probability - result) < resolution

    where:
    rv  | k   | p           | result
    1   | 0   | 0.5         | 0
    2   | 0   | 0.0         | 1
    2   | 0   | 0.0         | 1
    2   | 0   | 1.0         | 0
    2   | 1   | 0.0         | 0
    2   | 1   | 1.0         | 1
    2   | 1   | 0.5         | 0.5
    2   | 2   | 0.5         | 0
    7   | 7   | 0.5         | 0
    7   | 6   | 0.5         | 0.015625
    7   | 1   | 0.5         | 0.984375
    67  | 67  | 0.99        | 0
    67  | 67  | 0.01        | 0
    97  | 29  | 0.13        | 8.16092292681E-6
    600 | 200 | 1.0d / 6.0d | 0
  }
}
