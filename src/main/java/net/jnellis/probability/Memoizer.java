package net.jnellis.probability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntToDoubleFunction;

/**
 * User: Joe Nellis
 * Date: 8/14/2015
 * Time: 11:46 AM
 */
public class Memoizer {
  private final Map<Integer,Double> cache = new ConcurrentHashMap<>();

  private Memoizer() {}

  private IntToDoubleFunction  doMemoize(final IntToDoubleFunction function) {
    return key -> cache.computeIfAbsent(key, function::applyAsDouble);
  }

  public static IntToDoubleFunction memoize(final IntToDoubleFunction function) {
    return new Memoizer().doMemoize(function);
  }
}
