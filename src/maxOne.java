package GeneticAlgorithms;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/*
 * 		  [Author]
 * 	    Brandon Pike
 *        UTDallas
 *  Software Engineering
 */

public class maxOne {

	// Genetic constants
	private static int POP_SIZE = 20,
						SELECTS = 10,
						GENERATIONS = 500;
	private static double MUT_RATE = .5;
	/*
	 *  Genetic Algorithm to solve the Max One problem
	 *  Params: n - binary string size ; ("0001" if n=4)
	 *  Output: Best fit value after x GENERATIONS
	 */
	public static String maxOne(int n) {
		// Initialize Pop
		HashMap<String, Double> population = popInit(n);
		Map.Entry<String, Double> bestFit = null;
		for(int i=0; i<GENERATIONS; i++) {
			HashMap<String, Double> nextPop = new HashMap<String, Double>();
			// Mutation
			int muts = (int) (Math.ceil(MUT_RATE*population.size()));
			for(int m=0; m<muts; m++) {
				// get entry from remaining pop (and remove), mutate & add to nextPop
				Map.Entry<String, Double> entry = null;
				// keep track of key to remove
				String keyToRemove = null;
				// get 1 random key in population
				for(String key : population.keySet()) {
					keyToRemove = key;
					// mutate key and make sure its not solution or duplicate
					entry = mutate(key);
					while((nextPop.containsKey(entry.getKey()) || population.containsKey(entry.getKey())) 
							&& entry.getValue() != 1)
						entry = mutate(key);
					break;
				}
				population.remove(keyToRemove);
				nextPop.put(entry.getKey(), entry.getValue());
			}
			// Add remaining population
			population.putAll(nextPop);
			bestFit = getBestFit(population);
			if(bestFit.getValue() == 1) {
				System.out.print("Generation " + (i+1) + ": ");
				return bestFit.getKey();
			}
		}
		return bestFit.getKey();
	}
	/*
	 *  Key evolution function, mutate by flipping random bad value
	 *  Params: gene to be mutated
	 *  Output: mutated gene
	 */
	private static Entry<String, Double> mutate(String key) {
		//System.out.print("mutate: " + key);
		String s = "";
		Random r = new Random();
		int targetI = r.nextInt(key.length());
		while(key.charAt(targetI) == '1') { r = new Random(); targetI = r.nextInt(key.length()); }
		if(targetI == 0)
			s = "1" + key.substring(1, key.length());
		else
			s = key.substring(0, targetI) + "1" + key.substring(targetI+1, key.length());
		//System.out.println(" -> " + s);
		return new AbstractMap.SimpleEntry<String, Double>(s,getFitness(s));
	}

	// Testing purposes
	private static void printPopulation(HashMap<String, Double> population) {
		System.out.print("{");
		for(String key : population.keySet()) {
			System.out.print(key + ":" + population.get(key) + ", ");
		}
		System.out.print("}\n");
	}
	/*
	 *  Get the best fit gene in the population
	 *  Params: population - HashMap of <genes, fitness>
	 *  Output: Best fit entry in current population
	 */
	private static Entry<String, Double> getBestFit(HashMap<String, Double> pop) {
		Map.Entry<String, Double> bestFit = new AbstractMap.SimpleEntry<String, Double>("", Double.MIN_VALUE);
		for(String key : pop.keySet()) {
			double fitness = pop.get(key);
			bestFit = (fitness > bestFit.getValue()) ? bestFit = new AbstractMap.SimpleEntry<String, Double>(key, fitness) : bestFit;
		}
		return bestFit;
	}
	/*
	 *  Fitness method - how many 1's out of gene lenth
	 *  Params: gene - ("0001")
	 *  Output: fitness value out of 1 (# of 1's / gene.length())
	 */
	public static double getFitness(String gene) {
		int sum = 0;
		for(int i=0; i<gene.length(); i++) {
			sum += (gene.charAt(i) == '1') ? 1 : 0;
		}
		return ((double)sum / (double)gene.length());
	}
	
	/*
	 *  Randomly populate the starting population
	 *  Params: n - binary string size ; ("0001" if n=4)
	 *  Output: randomized population
	 */
	public static HashMap<String, Double> popInit(int n) {
		HashMap<String, Double> pop = new HashMap<String, Double>();
		Random r;
		for(int i=0; i<POP_SIZE; i++) {
			String s = "";
			r = new Random();
			for(int j=0; j<n; j++) {
				s += Integer.toString(r.nextInt(2));
			}
			pop.put(s, getFitness(s));
		}
		return pop;
	}
	
	public static void main(String args[]) {
		System.out.print(maxOne(10));
	}
}
