package net.jnellis.probability

import spock.lang.Specification

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
    def result = NegativeBinomial.probability(CumulativeOperation.equal, k, p, y)
    expect:
    Math.abs(result - exp) < resolution;

    where:
    y  | exp
    3  | 0.008
    4  | 0.0192
    5  | 0.03072
    10 | 0.0603979776
  }
}
