package net.jnellis.perf;

import net.jnellis.probability.NegativeBinomial;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static net.jnellis.probability.CumulativeOperation.lessThanOrEqual;

/**
 * User: Joe Nellis Date: 9/28/2019 Time: 5:15 PM
 */
@State(Scope.Benchmark)
public class NegativeBinomialBenchmark {

	@Param({"5", "50"})
	int successfulTrials;

	@Param({"0.05", "0.5", "0.95"})
	double chanceOfSuccess;

	@Param({"10", "100", "1000"})
	int totalTrials;

	@Benchmark
	public double negativeBinomial() {
		return NegativeBinomial.probability(successfulTrials,
		                                    chanceOfSuccess,
		                                    totalTrials);
	}

	@Benchmark
	public double negativeBinomialACM() {
		return new PascalDistribution(null, successfulTrials, chanceOfSuccess)
				.probability(totalTrials - successfulTrials);
	}

	@Benchmark
	public double cumulativeNegativeBinomial() {
		return new NegativeBinomial(lessThanOrEqual,successfulTrials, chanceOfSuccess)
				.getResult(totalTrials);
	}

	@Benchmark
	public double cumulativeNegativeBinomialACM() {
		return new PascalDistribution(null, successfulTrials, chanceOfSuccess)
				.cumulativeProbability(totalTrials - successfulTrials);
	}

}
