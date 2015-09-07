/*
 * PoissonTest.groovy
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
 * Date: 8/21/2015 
 * Time: 11:21 AM
 *
 */
class PoissonTest extends Specification {
  double resolution = 1.0E-12

  @Unroll
  def "test Poisson pdf with lambda: #lambda, P(Y=#y) = #result"() {

    expect:
    def probability = Poisson.probability(lambda, y)
    Math.abs(probability - result) < resolution

    where:
    lambda      | y | result
    0.1         | 0 | 0.90483741803596
    0.1         | 1 | 0.090483741803596
    1           | 0 | 0.367879441171442
    1           | 1 | 0.367879441171442
    Math.exp(1) | 0 | 0.0659880358452496
    Math.exp(1) | 1 | 0.179374078733909
  }

  @Unroll
  def "test Poisson cdf with lambda: #lambda, P(Y&lt;=#y) = #result"() {

    expect:
    def probability = lessThan.apply(y) { Poisson.probability(lambda, it) }
    Math.abs(probability - result) < resolution

    where:
    lambda      | y | result
    0.1         | 0 | 0.0
    0.1         | 1 | 0.90483741803596
    1           | 0 | 0.0
    1           | 1 | 0.367879441171442
    Math.exp(1) | 0 | 0.0
    Math.exp(1) | 1 | 0.06598803584524
  }

  def "test cumulative probability against TI-89 function TIStat.poissCdf"() {
    setup:
    double poissCdf = 0.423125414419
    double lambda = 5.1
    int Y = 4
    double result = new Poisson(lessThanOrEqual, lambda).getResult(Y)

    expect:
    Math.abs(result - poissCdf) < resolution
  }

  def "test exact probability against TI-89 function TIStat.poissPdf"() {
    setup:
    double poissPdf = 0.051648853532
    double lambda = 20
    int Y = 15
    double result = new Poisson(equal, lambda).getResult(Y)

    expect:
    Math.abs(result - poissPdf) < resolution
  }

  def "test extreme exact probability against TI-89 function TIStat.poissPdf"() {
    setup:
    double poissPdf = 0.00140910681
    double lambda = 79999
    int Y = 80011
    double result = new Poisson(equal, lambda).getResult(Y)

    expect:
    Math.abs(result - poissPdf) < resolution
  }


}
