package is.bthj.pacman.ga;

import is.bthj.pacman.behaviourtree.PacManBlackboard;

import java.util.Comparator;
import java.util.Random;

import GA_Sample.Gene;

public class PacManGeneForBehaviourTree implements Comparable<PacManGeneForBehaviourTree>, Comparator<PacManGeneForBehaviourTree> {

	/*
	 * Fitness evaluates how "close" the current gene is to the optimal solution,
	 * determined by the average PacMan score from several test runs. 
	 */
	private float fitness;
	
	
	/**
	 * Chromosome values.  
	 * Those PacMan behaviour threshold value placeholders (variables)
	 * constitute this gene's chromosome. 
	 */
	private int MIN_GHOST_DISTANCE;
	private int MAX_POWER_PILL_DISTANCE;
	private int POWER_PILL_WALK_AWAY_DISTANCE;
	
	/*
	 * Max threshold value to define a range to choose randomly from.
	 * Arbitrarily chosen as 100, as "most" values in data/distances
	 * are below 100 and they seemingly don't much reach higher than 150.
	 */
	private static final int DISTANCE_THRESHOLD_UPPER_BOUND = 100;
	
	
	/**
	 * Initializes this gene with zero fitness.
	 */
	PacManGeneForBehaviourTree() {
		
		fitness = 0;
	}
	
	/**
	 * Assigns a random value to each chromosome variable.
	 */
	public void randomizeChromosome() {
		
		Random rand = new Random();
		MIN_GHOST_DISTANCE = rand.nextInt(DISTANCE_THRESHOLD_UPPER_BOUND);
		MAX_POWER_PILL_DISTANCE = rand.nextInt(DISTANCE_THRESHOLD_UPPER_BOUND);
		POWER_PILL_WALK_AWAY_DISTANCE = rand.nextInt(DISTANCE_THRESHOLD_UPPER_BOUND);
	}
	
	/**
	 * Creates two offspring from two parents 
	 * by randomly choosing gene values from either parent;
	 * as a variant of crossover.
	 */
	public PacManGeneForBehaviourTree[] reproduce( PacManGeneForBehaviourTree other ) {
		PacManGeneForBehaviourTree[] result = new PacManGeneForBehaviourTree[2];
		
		PacManGeneForBehaviourTree child1 = new PacManGeneForBehaviourTree();
		PacManGeneForBehaviourTree child2 = new PacManGeneForBehaviourTree();
		
		Random rand = new Random();
		if( rand.nextFloat() < .5 ) {
			child1.setMinGhostDistance(this.getMinGhostDistance());
			child2.setMinGhostDistance(other.getMinGhostDistance());
		} else {
			child1.setMinGhostDistance(other.getMinGhostDistance());
			child2.setMinGhostDistance(this.getMinGhostDistance());
		}
		
		if( rand.nextFloat() < .5 ) {
			child1.setMaxPowerPillDistance(this.getMaxPowerPillDistance());
			child2.setMaxPowerPillDistance(other.getMaxPowerPillDistance());
		} else {
			child1.setMaxPowerPillDistance(other.getMaxPowerPillDistance());
			child2.setMaxPowerPillDistance(this.getMaxPowerPillDistance());
		}
		
		if( rand.nextFloat() < .5 ) {
			child1.setPowerPillWalkAwayDistance(this.getPowerPillWalkAwayDistance());
			child2.setPowerPillWalkAwayDistance(other.getPowerPillWalkAwayDistance());
		} else {
			child1.setPowerPillWalkAwayDistance(other.getPowerPillWalkAwayDistance());
			child2.setPowerPillWalkAwayDistance(this.getPowerPillWalkAwayDistance());
		}
		
		result[0] = child1;
		result[1] = child2;
		
		return result;
	}
	
	
	/**
	 * Mutates this gene by assigning a new random value to one of its chromosome values,
	 * with a probability of .125 for each of them.
	 */
	public void mutate() {
		Random rand = new Random();
		
		if( rand.nextFloat() < .125 ) {
			MIN_GHOST_DISTANCE = rand.nextInt(DISTANCE_THRESHOLD_UPPER_BOUND);
		}
		
		if( rand.nextFloat() < .125 ) {
			MAX_POWER_PILL_DISTANCE = rand.nextInt(DISTANCE_THRESHOLD_UPPER_BOUND);
		}
		
		if( rand.nextFloat() < .125 ) {
			POWER_PILL_WALK_AWAY_DISTANCE = rand.nextInt(DISTANCE_THRESHOLD_UPPER_BOUND);
		}
	}
	
	/**
     * Sets the fitness, after it is evaluated in the PacManGeneticAlgorithm class.
     * @param value: the fitness value to be set
     */
	public void setFitness( float value ) {
		fitness = value;
	}
	
	/**
     * @return the gene's fitness value
     */
	public float getFitness() {
		return fitness;
	}
	
	/**
	 * Corresponds the chromosome encoding to the phenotype, which is a representation
	 * that can be read, tested and evaluated by the main program.
	 * @return A PacMaBlackboard instance with threshold values set to this chromosome values. 
	 */
	public PacManBlackboard getPhenotype() {
		
		PacManBlackboard pacManBB = new PacManBlackboard(
				MIN_GHOST_DISTANCE, 
				MAX_POWER_PILL_DISTANCE, 
				POWER_PILL_WALK_AWAY_DISTANCE );
		
		return pacManBB;
	}
	
	
	public int compare(PacManGeneForBehaviourTree o1, PacManGeneForBehaviourTree o2) {

		int comparison;
		if( o1.getFitness() > o2.getFitness() ) {
			comparison = 1;
		} else if( o1.getFitness() < o2.getFitness() ) {
			comparison = -1;
		} else {
			comparison = 0;
		}
		return comparison;
	}

	public int compareTo(PacManGeneForBehaviourTree o) {

		return compare( this, o );
	}
	
	
	
	@Override
	public String toString() {
		
		return "MIN_GHOST_DISTANCE: " + MIN_GHOST_DISTANCE
				+ "\t MAX_POWER_PILL_DISTANCE: " + MAX_POWER_PILL_DISTANCE
				+ "\t POWER_PILL_WALK_AWAY_DISTANCE: " + POWER_PILL_WALK_AWAY_DISTANCE;
	}
	
	
	
	public int getMinGhostDistance() {
		return MIN_GHOST_DISTANCE;
	}

	public void setMinGhostDistance(int minGhostDistance) {
		MIN_GHOST_DISTANCE = minGhostDistance;
	}

	public int getMaxPowerPillDistance() {
		return MAX_POWER_PILL_DISTANCE;
	}

	public void setMaxPowerPillDistance(int maxPowerPillDistance) {
		MAX_POWER_PILL_DISTANCE = maxPowerPillDistance;
	}

	public int getPowerPillWalkAwayDistance() {
		
		return POWER_PILL_WALK_AWAY_DISTANCE;
	}

	public void setPowerPillWalkAwayDistance(int powerPillWalkAwayDistance) {
		POWER_PILL_WALK_AWAY_DISTANCE = powerPillWalkAwayDistance;
	}
}
