/*
 * HyperGeometricTest.groovy
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability

import spock.lang.Specification
import spock.lang.Unroll

import static net.jnellis.probability.CumulativeOperation.equal
import static net.jnellis.probability.CumulativeOperation.lessThan
import static net.jnellis.probability.CumulativeOperation.lessThanOrEqual

/**
 * User: Joe Nellis
 * Date: 8/24/2015 
 * Time: 1:47 PM
 *
 */
class HyperGeometricTest extends Specification {
  double resolution = 1.0E-12

  @Unroll
  def "test #N samples, #n samplesChosen, #r specialSamples, P(y=#y) is #result"() {
    setup:

    expect:
    double probability = new HyperGeometric(equal, N, n, r).getResult(y)
    Math.abs(probability - result) < resolution

    where:
    N   | n   | r  | y  | result
    1   | 1   | 1  | 0  | 0d
    1   | 1   | 1  | 1  | 1d
    2   | 1   | 1  | 0  | 0.5d
    2   | 1   | 1  | 1  | 0.5d
    9   | 3   | 3  | 3  | 0.011904761904761897d
    55  | 31  | 5  | 3  | 0.35662697149933564d
    277 | 157 | 57 | 33 | 0.116862766002871d
    301 | 300 | 30 | 30 | 0.900332225913621d
    10  | 7   | 5  | 1  | 0d
  }

  def "test chance to only win two bucks back in lottery"() {
    setup:
    def N = 50 // fifty numbers
    def n = 6  // of which only six are chosen
    def r = 6  // and of which six are special.
    def y = 2  // probability to only match two numbers
    def result = new HyperGeometric(equal, N, n, r).getResult(y);
    expect:
    Math.abs(result - 0.128141932073477) < resolution
  }

  def "test chance to win anything back in lottery P(y>=2)"() {
    setup:
    int N = 50 // fifty numbers
    int n = 6  // of which only six are chosen
    int r = 6  // and of which six are special.
    int y = 2  // probability to match all six
    def result = CumulativeOperation.greaterThanOrEqual
                                    .apply({ HyperGeometric.probability(N, n, r, it) }, y)

    expect:
    Math.abs(result - 0.145720452843487) < resolution
  }


  @Unroll
  def "test cumulative #N samples, #n samplesChosen, #r specialSamples, P(y<#y) is #result"() {
    setup:
    System.out.println("test cumulative $N samples, $n samplesChosen, $r specialSamples, P(y<$y) " +
        "is $result")

    expect:
    double probability = new HyperGeometric(lessThan, N, n, r).getResult(y)
    Math.abs(probability - result) < resolution

    where:
    N    | n   | r   | y  | result
    2    | 1   | 1   | 0  | 0d
    2    | 1   | 1   | 1  | 0.5d
    1    | 1   | 1   | 0  | 0d
    1    | 1   | 1   | 1  | 0d
    9    | 3   | 3   | 3  | 0.988095238095238d
    55   | 31  | 5   | 3  | 0.377453351926161d
    277  | 157 | 57  | 33 | 0.521499976361215d
    301  | 300 | 30  | 30 | 0.0996677740863789d
    1000 | 400 | 200 | 80 | 0.469133657102301d
  }

  def "test cumulative high population"() {
    setup:
    def N = 80000
    def n = 40000
    def r = 10000
    def y = 10000
    def result = new HyperGeometric(lessThanOrEqual, N, n, r).getResult(y)
    println "error is ${result - 1.0}"
    expect:
    assert result != 0
    Math.abs(result -  1.0)< resolution
  }

  def "test chance to win CA Lotto jackpot"() {
    setup:
    int N = 47  // five numbers between 1 to 47
    int n = 5
    int r = 5
    int y = 5
    def result = HyperGeometric.probability(N, n, r, y)
    // mega number is 1-27, of which we need one. so 1/27.
    result = result / 27
    println "chance to win jackpot is 1 in ${(int)DiscreteProbability.reciprocal(result)}"
    expect:
    // ca lotto web site says roughly 1 in 42million
    Math.abs(result - 1/42000000) <  1.0E-9   // within a million
  }
}
