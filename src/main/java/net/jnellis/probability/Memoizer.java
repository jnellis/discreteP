/*
 * Memoizer.java
 *
 * Copyright (c) 2015. Joe Nellis
 * Distributed under MIT License. See accompanying file License.txt or at
 * http://opensource.org/licenses/MIT
 */

package net.jnellis.probability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntToDoubleFunction;

/**
 * A memoizer for Probability classes.
 */
public class Memoizer {
  private final Map<Integer,Double> cache = new ConcurrentHashMap<>();

  private Memoizer() {}

  /**
   * Creates a memoized probability function, or any integer to double function.
   * <p>
   * <pre>
   * Probability pdf = y -&gt; HyperGeometric.probability(80000,40000,10000,y);
   * Probability memoizedPdf = (Probability)Memoizer.memoize(pdf);
   * </pre>
   *
   * @param pdf The probability distribution function to be memoized.
   * @return a memoized IntToDouble function. Can be cast to Probability.
   */
  public static IntToDoubleFunction memoize(final IntToDoubleFunction pdf) {
    return new Memoizer().doMemoize(pdf);
  }

  private IntToDoubleFunction  doMemoize(final IntToDoubleFunction function) {
    return key -> cache.computeIfAbsent(key, function::applyAsDouble);
  }
}
