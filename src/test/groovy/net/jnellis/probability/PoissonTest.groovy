package net.jnellis.probability

import spock.lang.Specification
import static net.jnellis.probability.CumulativeOperation.*

/**
 * User: Joe Nellis
 * Date: 8/21/2015 
 * Time: 11:21 AM
 *
 */
class PoissonTest extends Specification {
  double  resolution = 1.0E-12

  def "test cumulative probability against TI-89 function TIStat.poissCdf"() {
    setup:
    double poissCdf =  0.423125414419
    double lambda = 5.1
    int Y = 4
    double result = new Poisson(lessThanOrEqual, lambda).getResult(Y)
    println result

    expect:
    Math.abs(result - poissCdf) < resolution
  }

  def "test exact probability against TI-89 function TIStat.poissPdf"() {
    setup:
    double poissPdf =  0.051648853532
    double lambda = 20
    int Y = 15
    double result = new Poisson(equal, lambda).getResult(Y)
    println result

    expect:
    Math.abs(result - poissPdf) < resolution
  }

  def "test extreme exact probability against TI-89 function TIStat.poissPdf"() {
    setup:
    double poissPdf =  0.00140910681
    double lambda = 79999
    int Y = 80011
    double result = new Poisson(equal, lambda).getResult(Y)
    println result

    expect:
    Math.abs(result - poissPdf) < resolution
  }
}
