package net.jnellis.probability;

import java.util.function.IntToDoubleFunction;
import java.util.stream.IntStream;

/**
 * User: Joe Nellis
 * Date: 8/21/2015
 * Time: 9:49 AM
 */
@FunctionalInterface
public interface CumulativeOperation {
  double apply(IntToDoubleFunction probabilityFunction, int randomVariable);

  CumulativeOperation equal = (p, rv) -> p.applyAsDouble(rv);

  CumulativeOperation lessThan =
      (p, rv) -> IntStream.range(0, rv)
                          .mapToDouble(p::applyAsDouble)
                          .sum();

  CumulativeOperation lessThanOrEqual =
      (p, rv) -> IntStream.rangeClosed(0, rv)
                          .mapToDouble(p::applyAsDouble)
                          .sum();

  CumulativeOperation notEqual =
      (p, rv) -> 1.0 - CumulativeOperation.equal.apply(p, rv);

  CumulativeOperation greaterThan =
      (p, rv) -> 1.0 - CumulativeOperation.lessThanOrEqual.apply(p, rv);

  CumulativeOperation greaterThanOrEqual =
      (p, rv) -> 1.0 - CumulativeOperation.lessThan.apply(p, rv);

}
