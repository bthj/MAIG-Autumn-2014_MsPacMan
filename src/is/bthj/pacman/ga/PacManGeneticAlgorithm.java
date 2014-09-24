package is.bthj.pacman.ga;

import is.bthj.pacman.behaviourtree.PacManBlackboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

public class PacManGeneticAlgorithm {

	// constants
	
	static int POPULATION_SIZE = 500;
	static int MIN_MAX_FITNESS_DELTA_GOAL = 100; // worst and best fitness should be this close
	
	
	// variables
	
	ArrayList<PacManGeneForBehaviourTree> population;
	
	Executor exec;
	
	
	// functions
	
	/**
	 * Creates the starting population of {@link PacManGeneForBehaviourTree} classes,
	 * whose chromosome contents are random.
	 * @param size The size of the population.
	 */
	public PacManGeneticAlgorithm( int size ) {
		
		population = new ArrayList<PacManGeneForBehaviourTree>();
		for( int i = 0; i < size; i++ ) {
			
			PacManGeneForBehaviourTree entry = new PacManGeneForBehaviourTree();
			entry.randomizeChromosome();
			population.add( entry );
		}
		
		 exec = new Executor();
	}
	
	/**
	 * For all members of the population, runs a heuristic that evaluates their fitness
     * based on their phenotype.
	 */
	public void evaluateGeneration() {
		
		for( int i = 0; i < population.size(); i++ ) {
			// evaluation of the fitness function for each gene in the population
			
			PacManGeneForBehaviourTree oneGene = population.get( i );
			oneGene.setFitness( fitnessFunction(oneGene) );
		}
		Collections.sort( population );
	}
	
	private float fitnessFunction( PacManGeneForBehaviourTree gene ) {
		
		// we'll obtain this gene's fitness by running PacMan experiments
		// x times, using a BehaviourTree with threshold values set to those
		// from the gene to be tested for fitness.
		float fitness = (float) exec.runExperimentReturnAverageScore(
				new GeneticAlgorithmPacManController(gene), 
				new StarterGhosts(), 
				1 ); // <- we run the experiment 1 times
		
//		if( 0 == fitness ) {
//			System.out.println( "zero fitness: " + gene.toString() );
//		}
		
		return fitness;
	}
	
	
	/**
	 * With each gene's fitness as a guide, chooses which genes should mate and produce offspring.
     * The offspring are added to the population, replacing the previous generation's Genes completely.
	 */
	public void produceNextGeneration() {
		
		PacManGeneForBehaviourTree[] parentSelection = parentSelection();
		PacManGeneForBehaviourTree parent1 = parentSelection[0];
		PacManGeneForBehaviourTree parent2 = parentSelection[1];
		
		PacManGeneForBehaviourTree[] children = parent1.reproduce( parent2 );
		PacManGeneForBehaviourTree child1 = children[0];
		PacManGeneForBehaviourTree child2 = children[1];
		child1.mutate();
		child2.mutate();
		
		// let's remove the two worst genes
		population.remove( 0 );
		population.remove( 0 );
		// and add the children
		population.add( child1 );
		population.add( child2 );
	}
	
	/**
	 * Selects the best out of random 5 parents.
	 * @return Two parents according to selection process.
	 */
	private PacManGeneForBehaviourTree[] parentSelection() {
		PacManGeneForBehaviourTree[] result = new PacManGeneForBehaviourTree[2];
		
		// let's find 5 random indices from the population
		int[] parentIndicies = new int[5];
		for( int i=0; i < parentIndicies.length; i++ ) parentIndicies[i] = -1;
		
		Random rand = new Random();
		for( int i=0; i < parentIndicies.length; i++ ) {
			
			int parentIndex = -1;
			do {
				parentIndex = rand.nextInt( population.size() );
			} while( doesIndexExistInArray(parentIndex, parentIndicies) );
			parentIndicies[i] = parentIndex;
		}
		ArrayList<PacManGeneForBehaviourTree> randomGenes = new ArrayList<PacManGeneForBehaviourTree>();
		for( int oneIndex : parentIndicies ) {
			randomGenes.add( population.get(oneIndex) );
		}
		// let's sort the random 5 parents according to fitness and choose the best two
		Collections.sort( randomGenes );
		result[0] = randomGenes.get( randomGenes.size() - 2 );
		result[1] = randomGenes.get( randomGenes.size() - 1 );
		
		return result;
	}
	
	private boolean doesIndexExistInArray( int index, int[] array ) {
    	boolean exists = false;
    	for( int oneIndex : array ) {
    		if( oneIndex == index ) {
    			exists = true;
    			break;
    		}
    	}
    	return exists;
    }
	
	
	// accessors
	
	/**
	 * 
	 * @return The size of the population.
	 */
	public int size() {
		return population.size();
	}
	
	/**
	 * Returns gene at position <b>index</b> in the population.
	 * @param index The position in the population.
	 * @return The gene at position <b>index</b> of the population.
	 */
	public PacManGeneForBehaviourTree getGene( int index ) {
		return population.get( index );
	}
	
	// Genetic Algorithm testing method
	public static void main(String[] args) {
		
		// Initialize the population
		PacManGeneticAlgorithm population = new PacManGeneticAlgorithm(POPULATION_SIZE);
		int generationCount = 0;
		
		// we'll break out of this loop with a break statement from within it
		// when the worst and best fitness values of genes are close enough 
		//  TODO: define "enough"
		while( true ) {
			
			population.evaluateGeneration();
			
			// print information on the current state of the population
			float avgFitness = 0.f;
            float minFitness = Float.POSITIVE_INFINITY;
            float maxFitness = Float.NEGATIVE_INFINITY;
            
            for( int i = 0; i < population.size(); i++ ) {
            	float currentFitness = population.getGene(i).getFitness();
            	avgFitness += currentFitness;
            	if( currentFitness < minFitness ) {
            		minFitness = currentFitness;
            	}
            	if( currentFitness > maxFitness ) {
            		maxFitness = currentFitness;
            	}
            }
            if( population.size() > 0 ) {
            	avgFitness = avgFitness / population.size();
            }
            
            String output = "Generation: " + generationCount
            		+ "\t AvgFitness: " + avgFitness
            		+ "\t MinFitness: " + minFitness
            		+ "\t MaxFitness: " + maxFitness;
            System.out.println( output );
            
        	System.out.println( "\tWorst performing gene so far: " + population.getGene(0).toString() );
        	System.out.println( "\tBest performing gene so far: " + population.getGene(population.size()-1).toString() );
            
            if( (maxFitness - minFitness) <= MIN_MAX_FITNESS_DELTA_GOAL ) {
            	
            	// we have reached our goal, let's print the best gene
            	System.out.println( "Best performing gene:" );
            	System.out.println( population.getGene(0).toString() );
            	
            	break;
            }
            
            population.produceNextGeneration();
            generationCount++;
		}
	}
	
	
}
