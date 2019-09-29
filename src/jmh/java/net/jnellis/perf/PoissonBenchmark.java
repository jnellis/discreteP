package net.jnellis.perf;

import net.jnellis.probability.Poisson;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static net.jnellis.probability.CumulativeOperation.lessThanOrEqual;

/**
 * User: Joe Nellis Date: 9/28/2019 Time: 5:40 PM
 */
@State(Scope.Benchmark)
public class PoissonBenchmark {

	@Param({"5", "50", "500"})
	int lambda;

	@Param({"3", "75", "300"})
	int rv;

	@Benchmark
	public double poisson() {
		return Poisson.probability(lambda, rv);
	}

	@Benchmark
	public double poissonACM() {
		return new PoissonDistribution(lambda).probability(rv);
	}

	@Benchmark
	public double cumulativePoisson() {
		return new Poisson(lessThanOrEqual,lambda).getResult(rv);
	}

	@Benchmark
	public double cumulativePoissonACM() {
		return new PoissonDistribution(lambda).cumulativeProbability(rv);
	}
}
