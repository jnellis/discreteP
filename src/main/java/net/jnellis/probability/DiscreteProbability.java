package net.jnellis.probability;

import java.util.Optional;

/**
 * User: Joe Nellis
 * Date: 8/12/2015
 * Time: 6:48 PM
 */
public abstract class DiscreteProbability implements Probability  {


  private final net.jnellis.probability.CumulativeOperation rvOperation;


  DiscreteProbability(net.jnellis.probability.CumulativeOperation rvOperation) {
    this.rvOperation = Optional.of(rvOperation).get();
  }

  @Override
  public net.jnellis.probability.CumulativeOperation getCumulativeRandomVariable() {
    return rvOperation;
  }


  abstract double getExpectedValue();

  abstract double getVariance();

}
