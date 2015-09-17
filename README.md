# Compute discrete probabilities. #

## Five distributions: ## 
Binomial, Geometric, HyperGeometric, NegativeBinomial, and Poisson

Classes are structured around the concept of a probability distribution function with one
random variable, Y. Static methods exist for each distribution for computing probability 
or cumulative probabilities if a distribution object is not needed. 

## Example as a class or as static methods ##
Create a probability distribution for a random variable number of successful trials.

    DiscreteProbability pdf = new Binomial(CumulativeOperation.lessThan, 5, 0.166667);

* The total number of trials is five and each trial has a 1 in 6 chance of success. 
* The cumulative operation is 'lessThan' so it reports the cumulative probability of the 
chance of 'less than' Y successful trials happening in five total trials.  

 
    // probability of less than four successful trials out of five
    double lessThan4 = pdf.getResult(4);
    double lessThan3 = pdf.getResult(3);
    double meanValue = pdf.getExpectedValue();
    double variance = pdf.getVariance();
    
The class based operation is easier to use when computing multiple random variables on the same 
distribution. The expected value (mean) and variance can also be computed. For single probability
or cumulative probability you can use a static method which doesn't create an intermediary object.

    // exactly four successes out of five trials
    double resultAt4 = Binomial.probability(5, 0.1666667, 4);
    // use lambdas to create cumulative probabilities. 
    double lessThan4 = CumulativeOperation.lessThan
                                          .apply(4,  y-> Binomial.probability(5, 0.1666667, y));
                                          
You can also use the cumulative static method on a Probability object that has a different 
cumulative operation, by providing it with just the method it uses for computing a single 
probability.
    
    double greaterThanOrEqualTo3 = CumulativeOperation.greaterThanOrEqual
                                                      .apply(3,pdf::computeResult);


## Binomial ##
The binomial probability models the probability of a number of successful trials, 
out of a total number of trials. 
Each trial has the EXACT same chance of success or failure. For example rolling a die, 
the chance to roll a one is 1/6.
Let's say we have five die or we are rolling the same die over 
five times and we want to know the probability that a one comes up exactly three out of the 
five times. Three being the number of successful trials of rolling a one. 

    Binomial pdf = new Binomial(CumulativeOperation.equal, 5, 1.0/6)
    double result = pdf.getResult(3) // 3.215020577803498%  
    
    // three or less ones rolled. 
    double cumResult = CumulativeOperation.lessThanOrEqual
                                          .apply(3, pdf::computeResult); // 99.66563785982512% 
                                           
## Geometric ##
        
The geometric probability models the number of the trial for which an event succeeds (or fails).
Lets assume that a weapon has a 2% chance of misfiring in competition. What is the chance the 
weapon will misfire on the 4th shot? Here we only care about the 4th shot, P(Y=4) but we could use 
the cumulative P(Y<=4) to see the chance of a misfiring on or before the 4th shot.
When we say P(Y=4) we are saying "what is the chance for a fire,fire,fire then a misfire?" 

    Geometric.probability(0.02, 4);  // P(Y=4) = 1.8823839999999998% 
    
    // almost 8% chance to misfire on or before the 4th shot.
    new Geometric(lessThanOrEqual, 0.02).getResult(4); // P(Y<=4) = 7.763184000000001%
    
## Negative Binomial ##

The negative binomial probability is similar to the geometric and models the number of the trial
for which the Kth event succeeds (or fails). Like the last example we could ask what is the 
chance that the weapon misfires three times by the 20th shot. This means that there will be two 
misfires within the first 19 shots and that the last shot is the third misfire. Chance of any shot
misfiring is still 2%.

    NegativeBinomial.probability(3, 0.02, 20); // P(Y=20) = 0.09703521761351235%, less than .1%
    
    // chance for 3 misfires in 20 or less shots
    new NegativeBinomial(lessThanOrEqual, 3, 0.02).getResult(20); // P(Y<=20) = 0.7068693403814062%
    
## Poisson ##
The poisson probability models events based around a known average. The average is based on 
events that can happen at anytime and are considered instantaneous (meaning two events don't 
happen at the exact same time). For example, the number of typing errors per page or the number of 
traffic accidents at a street intersection in a year have an average that is Poisson-"ish". 
If we know that a certain intersection has an average of 7 traffic accidents a year and we want to
output what is the probability that there will be 5 or less accidents this year... 

    new Poisson(lessThanOrEqual, 7).getResult(5); // P(Y=5) = 30.07082761743611% 

## HyperGeometric ##
The hypergeometric probability models a more complex event choosing from two sets. It is also the 
probability that models modern day lotteries. Suppose we have a bag with 3 red chips and 5 black 
chips. We want to know the chance of picking one red chip out of three picks. The bag is dark and 
picking from the bag is assumed random and chips are not replaced after picking from the bag.
The HyperGeometric probability considers 4 arguments.
   
* the population size, which is 8 chips total, 3 red and 5 black
* the sample size, we are making 3 picks (this assumes chips are not put back into the bag after
 picking!!)
* the set of items we are interested in. We want red chips and there are 3 in the set.
* the number of items from the sample set we are interested in. We only care about getting EXACTLY
 one. If we wanted to consider 1 or more or 2 or less or 3 or less then it would be a cumulative 
 probability. 
 
    HyperGeometric.probability(8,3,3,1);  // P(Y=3) = 53.57142857142857%
    
A more practical example might be finding the probability of an event in a legal case. 20 workers 
consisting of three minorities are chosen from to fill two types of positions, 16 regular labor 
positions and 4 heavy labor positions. The minorities claim in a lawsuit that all of them are 
chosen for heavy labor positions while the employer contends the assignments are random. To figure
out what the chance that all minorities are chosen for the heavy labor job we set our population 
size to 20. The sample size is 4, for the number of heavy labor positions. The set of items we are 
interested in is 3, for the number of minorities and we are interested in when 
P(Y=3) or when all the minorities are chosen.
    
    HyperGeometric.probability(20, 4, 3, 3); // P(Y=3) =  0.35087719298245615% 

So we see that the chance of all minorities getting picked for heavy labor positions is less
than 1% so the minorities have a solid discrimination case.


    
    