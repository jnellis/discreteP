package net.jnellis.perf;

import net.jnellis.probability.Geometric;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static net.jnellis.probability.CumulativeOperation.lessThanOrEqual;

/**
 * User: Joe Nellis Date: 9/28/2019 Time: 5:08 PM
 */
@State(Scope.Benchmark)
public class HyperGeometricBenchmark {

	@Param({"0.05","0.5","0.937"})
	double chanceOfSuccess;

	@Param({"5","50","500"})
	int onTrial;

	@Benchmark
	public double hyperGeometric(){
		return Geometric.probability(chanceOfSuccess, onTrial);
	}

	@Benchmark
	public double hyperGeometricACM(){
		return new GeometricDistribution(null,chanceOfSuccess).probability(onTrial);
	}
	@Benchmark
	public double cumulativeHyperGeometric(){
		return new Geometric( lessThanOrEqual,chanceOfSuccess).getResult(onTrial);
	}

	@Benchmark
	public double cumulativeHyperGeometricACM(){
		return new GeometricDistribution(null,chanceOfSuccess).cumulativeProbability(onTrial);
	}



}
