package net.jnellis.probability;

/**
 * User: Joe Nellis
 * Date: 8/27/2015
 * Time: 6:30 PM
 */
public class NegativeBinomial extends DiscreteProbability {

  final int k;
  final double p;

  /*	The Negative Binomial Probability
    The equation for this probability is:
    P(Y) = (Y-1)!/(((Y-1)-(K-1))!(K-1)!)*p^K*(1-p)^(Y-K)    */
  public NegativeBinomial(CumulativeOperation rvOperation,
                          int kSuccessfulTrials,
                          double pChanceOfSuccess) {
    super(rvOperation);
    this.k = kSuccessfulTrials;
    this.p = pChanceOfSuccess;
  }


  @Override
  public double computeResult(int randomVariable) {
    return probability( k, p, randomVariable);
  }


  /*	Constructor Parameters:
      y - number of the trial that the kth success happens; must be 0 <= Y
      k - number of successful trials; must be 0 <= K <= Y
      p - chance of success for each trial; must be 0.0 <= p <= 1.0
      rvOperation - Random variable comparison. Whether this probability is cumulative and which
      way it is.
  */
  public static double probability(int k, double p, int y) {
    // the base class function GetResult will possibly
    // set our number of trials below K so just return 0.0;
    if (k > y || y == 0)
      return 0.0;

    // initialize some variables
    double result = 1.0;
    double q = 1.0 - p;
    int range = 0, np = 0, nq = 0, nnumer = 0, ndenom = 0;
    // validate
    assert (k <= y && k >= 0);
    assert (p <= 1.0 && p >= 0.0);
    // check optimizations
    if (k == 1) {
      return result = Math.pow(q, y);
    }
    if (k == y) {
      return result = Math.pow(p, k);
    }
    // P(Y) = (Y-1)!/(((Y-1)-(K-1))!(K-1)!)*p^K*(1-p)^(Y-K)

    // reorder the factorials to account for cancellations
    // in numerator and denominator.
    if (k < y - k) {
      range = k - 1;    // Y-K cancels out
    } else {
      range = (y - 1) - (k - 1);  // K cancels out
    }
    np = k;
    nq = y - k;
    ndenom = range;
    nnumer = y - 1;

    while (np > 0 || nq > 0 || ndenom > 0 || nnumer > (y - 1 - range)) {
      // If the result is greater than one we want to divide by 
      // a denominator digit or multiply by percentage p or q.
      // If we are out of numerator digits then finish multiplying
      // with our powers of p or q or dividing by a denom digit.
      if (result >= 1.0 || nnumer == (y - 1 - range)) {
        if (ndenom > 0) {
          //m_resut *= (1.0/ndenom);
          result /= ndenom;
          --ndenom;
        } else if (nq > 0) {
          result *= q;
          --nq;
        } else if (np > 0) {
          result *= p;
          --np;
        } else {
          throw new IllegalStateException("Binomial Probability computation error- check success " +
                                              "percentage between 0 and 1");
        }
      }
      // If the result is less than one then we want to multiply
      // by a numerator digit. If we are out of denominator digits,
      // powers of p or powers of q then multiply rest of result 
      // by numerator digits.
      else if (result < 1.0 || np == 0 /* || nq ==0 || ndenom ==0 */) {
        if (nnumer > (y - 1 - range)) {
          result *= nnumer;
          --nnumer;
        } else {
          throw (new IllegalStateException("Binomial Probability computation error- unknown error"));
        }
      } else {
        throw (new IllegalStateException("Binomial Probability computation error- possible value " +
                                        "infinity or YaY"));
      }
    }
    return result;
  }

  // The Expected Value is defined by
// E(Y) = K / p
  @Override
  double getExpectedValue() {
    return k / p;
  }

  // The variance is defined by
// o^2 = K*(1-p)/p^2
  @Override
  double getVariance() {
    return k * (1 - p) / (p * p);
  }
}
