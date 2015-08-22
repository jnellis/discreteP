package net.jnellis.probability
import spock.lang.Specification
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
    Geometric gp = new Geometric(CumulativeOperation.equal , chanceOfSuccess);
    expect:
    gp.getResult(trialOfSuccess) - 0.0294004851 < resolution
  }

  def "test high cumulative probability"(){
    setup:
    int trialOfSuccess = 500 //500th or lower
    int chanceOfSuccess = 0.00051d
    Geometric gp = new Geometric(CumulativeOperation.lessThanOrEqual, chanceOfSuccess);
    expect:
    gp.getResult(trialOfSuccess) - 0.225144906484 < resolution
  }
}
