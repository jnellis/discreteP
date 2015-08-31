package net.jnellis.probability

import spock.lang.Specification
import spock.lang.Unroll

import static net.jnellis.probability.CumulativeOperation.equal

/**
 * User: Joe Nellis
 * Date: 8/24/2015 
 * Time: 1:47 PM
 *
 */
class HyperGeometricTest extends Specification {
  double resolution = 1.0E-12

  @Unroll
  def "test #N samples, #n samplesChosen, #r specialSamples, P(y=#rv) is #result"() {
    setup:

    expect:
    Math.abs(new HyperGeometric(equal, N, n, r).getResult(rv) - result) < resolution

    where:
    N   | n   | r  | rv | result
    1   | 1   | 1  | 1  | 1
    9   | 3   | 3  | 3  | 0.011904761904761897
    55  | 31  | 5  | 3  | 0.35662697149933564
    277 | 157 | 57 | 33 | 0.116862766002871
    301 | 300 | 30 | 30 | 0.900332225913621
  }

  def "test chance to only win two bucks back in lottery"() {
    setup:
    def N = 50 // fifty numbers
    def n = 6  // of which only six are chosen
    def r = 6  // and of which six are special.
    def y = 2  // probabilty to only match two numbers
    def result = new HyperGeometric(equal, N, n, r).getResult(y);
    expect:
    Math.abs(result - 0.128141932073477) < resolution
  }
}
