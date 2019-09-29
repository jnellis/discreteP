/*
 * MemoizerTest.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

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
    // choose the longest running cumulative test.
    Probability pdf = y -> HyperGeometric.probability(80000, 40000, 10000, y);
    IntToDoubleFunction memoizedPdf = Memoizer.memoize(pdf::computeResult);

    long start = System.nanoTime();
    CumulativeOperation.lessThanOrEqual.apply(10000, memoizedPdf);
    long end = System.nanoTime();
    long diff1 = end - start;
    //System.out.println("Memoizer test: first pass " + diff1 + "ms.");

    start = System.nanoTime();
    CumulativeOperation.lessThanOrEqual.apply(10000, memoizedPdf);
    end = System.nanoTime();
    long diff2 = end - start;
    //System.out.println("Memoizer test: second pass " + diff2 + "ms.");

    // is it at least 100 times faster?
    assert diff1 > 100 * diff2;

  }
}