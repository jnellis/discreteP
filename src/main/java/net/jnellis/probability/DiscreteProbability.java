package net.jnellis.probability;

import java.util.Objects;

/**
 * User: Joe Nellis
 * Date: 8/12/2015
 * Time: 6:48 PM
 */
public abstract class DiscreteProbability implements Probability  {


  private final CumulativeOperation rvOperation;


  DiscreteProbability(CumulativeOperation rvOperation) {
    this.rvOperation = Objects.requireNonNull(rvOperation,
                                              "rvOperation can't be null.");
  }

  @Override
  public CumulativeOperation getCumulativeRandomVariable() {
    return rvOperation;
  }


  abstract double getExpectedValue();

  abstract double getVariance();

}
