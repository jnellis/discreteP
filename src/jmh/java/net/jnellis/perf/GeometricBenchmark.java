package net.jnellis.perf;

import net.jnellis.probability.CumulativeOperation;
import net.jnellis.probability.Geometric;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * User: Joe Nellis Date: 9/28/2019 Time: 2:13 PM
 */
@State(Scope.Benchmark)
public class GeometricBenchmark {

	@Param({".05", "0.5","0.937"})
	double chanceOfSuccess;

	@Param ({"5","100","500"})
	int onTrial;

	@Benchmark
	public double geometric(){

		return Geometric.probability(chanceOfSuccess, onTrial);
	}

	@Benchmark
	public double geometricACM(){

		return new GeometricDistribution(null,chanceOfSuccess).probability(onTrial-1);
	}

	@Benchmark
	public double cumulativeGeometric(){

		return new Geometric(CumulativeOperation.lessThanOrEqual,
		                     chanceOfSuccess).getResult(onTrial);
	}

	@Benchmark
	public double cumulativeGeometricACM(){

		return new GeometricDistribution(null,chanceOfSuccess)
				.cumulativeProbability(onTrial-1);
	}
}
