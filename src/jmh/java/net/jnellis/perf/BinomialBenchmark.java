package net.jnellis.perf;

import net.jnellis.probability.Binomial;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.openjdk.jmh.annotations.*;

import java.util.BitSet;

import static net.jnellis.probability.CumulativeOperation.lessThanOrEqual;

/**
 * User: Joe Nellis Date: 9/21/2019 Time: 5:56 PM
 */

@State(Scope.Benchmark)
public class BinomialBenchmark {
	
	@Param({"0","1","2"})
	int idx;

	final int[][] vals = {{40,20},{100,40 },{500 ,5}, {500,251}};

	int trials;

	@Param({"0.013","0.5",".95"})
	double chanceOfSuccess ;

	int rv;


	BitSet primes;
	int[] factors;

	@Setup
	public void setup(){
		trials = vals[idx][0];
		rv = vals[idx][1];
	}

	@Benchmark
	public double binomialBenchmark(){
		return Binomial.probability(trials,chanceOfSuccess,rv);
	}

	@Benchmark
	public double acmBinomialBenchmark(){
		return new BinomialDistribution(null, trials,chanceOfSuccess).probability(rv);
	}


	@Benchmark
	public double cumulativeBinomialBenchmark(){
		return new Binomial(lessThanOrEqual,trials,chanceOfSuccess).computeResult(rv);
	}

	@Benchmark
	public double cumulativeAcmBinomialBenchmark(){
		return new BinomialDistribution(null, trials,chanceOfSuccess).cumulativeProbability(rv);
	}



}
