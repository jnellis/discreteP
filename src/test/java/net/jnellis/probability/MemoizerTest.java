package net.jnellis.probability;

import org.junit.Test;

import java.util.function.IntToDoubleFunction;

/**
 * User: Joe Nellis
 * Date: 8/21/2015
 * Time: 9:05 AM
 */
public class MemoizerTest {

  @Test
  public void testMemoize() throws Exception {
    Binomial binomial = new Binomial(CumulativeOperation.equal,
                                     8009,.5);
    IntToDoubleFunction longComputeTimeFunction = binomial::computeResult;
    IntToDoubleFunction memoizedFunction = Memoizer.memoize(longComputeTimeFunction);



  }
}