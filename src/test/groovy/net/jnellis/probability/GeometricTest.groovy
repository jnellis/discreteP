/*
 * GeometricTest.groovy
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability

import org.apache.commons.math3.distribution.GeometricDistribution
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: Joe Nellis
 * Date: 8/22/2015 
 * Time: 3:50 PM
 *
 */
class GeometricTest extends Specification {
  double resolution = 1.0E-12

  def "test exact probability"() {
    setup:
    int trialOfSuccess = 5
    double chanceOfSuccess = 0.51d
    Geometric gp = new Geometric(CumulativeOperation.equal, chanceOfSuccess);
    expect:
    gp.getResult(trialOfSuccess) - 0.0294004851 < resolution
  }

  def "test high cumulative probability"() {
    setup:
    int trialOfSuccess = 500 //500th or lower
    int chanceOfSuccess = 0.00051d
    Geometric gp = new Geometric(CumulativeOperation.lessThanOrEqual, chanceOfSuccess);
    expect:
    gp.getResult(trialOfSuccess) - 0.225144906484 < resolution
  }

  def "Shooting yourself on the third round of russian roulette"() {
    expect:
    Math.abs(Geometric.probability(1.0d / 6, 3) - (25.0d / 216)) < resolution
  }

  @Unroll
  def "Test against apache commons math (onTrial=#onTrial)"() {
    expect:
    // apache commons math version of geometric distribution uses number of failures before success
    // hence the onTrial-1 term.
    def expected = new GeometricDistribution(null, chanceOfSuccess).probability(onTrial-1)
    def result = Geometric.probability(chanceOfSuccess, onTrial)
    Math.abs(expected - result) < resolution
//    println expected
//    println result
//    println()
    where:

    onTrial | chanceOfSuccess
    50      | 0.50d
    35      | 0.5d    // apache commons math fails are low number of trials?
    997     | 0.0005d
    8009    | 0.5d
    80009   | 0.5d
    800009  | 0.5d
  }

  @Unroll
  def "Cumulative Test against apache commons math (onTrial=#onTrial)"() {
    expect:
    // apache commons math version of geometric distribution uses number of failures before success
    // hence the onTrial-1 term.
    def expected = new GeometricDistribution(null, chanceOfSuccess).cumulativeProbability(onTrial-1)
    def result = new Geometric( CumulativeOperation.lessThanOrEqual , chanceOfSuccess).getResult(onTrial)
//    println expected
//    println result
//    println()
    Math.abs(expected - result) < resolution

    where:

    onTrial | chanceOfSuccess
    50      | 0.00050d
    30      | 0.0005d
    997     | 0.0005d
    8009    | 0.00002d
    80009   | 0.00002d
  }
}
