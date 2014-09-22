package GA_Sample;

import java.util.Comparator;
import java.util.Random;

public class Gene implements Comparator<Gene>, Comparable<Gene> {
    // --- variables:

    /**
     * Fitness evaluates to how "close" the current gene is to the
     * optimal solution (i.e. contains only 1s in its chromosome)
     * A gene with higher fitness value from another signifies that
     * it has more 1s in its chromosome, and is thus a better solution
     * While it is common that fitness is a floating point between 0..1
     * this is not necessary: the only constraint is that a better solution
     * must have a strictly higher fitness than a worse solution
     */
    private float mFitness;
    /**
     * The chromosome contains only integers 0 or 1 (we choose to avoid
     * using a boolean type to make computations easier)
     */
    protected int mChromosome[];

    // --- functions:
    /**
     * Allocates memory for the mChromosome array and initializes any other data, such as fitness
     * We chose to use a constant variable as the chromosome size, but it can also be
     * passed as a variable in the constructor
     */
    Gene() {
        // allocating memory for the chromosome array
        mChromosome = new int[GeneticAlgorithm.CHROMOSOME_SIZE];
        // initializing fitness
        mFitness = 0.f;
    }

    /**
     * Randomizes the numbers on the mChromosome array to values 0 or 1
     */
    public void randomizeChromosome(){
        // code for randomization of initial weights goes HERE
    	
    	// bthj:
    	// let's have a 50 / 50 chance of chromosome values being 1 or 0
    	Random rand = new Random();
    	for( int i=0; i < mChromosome.length; i++ ) {
    		if( rand.nextFloat() < .5 ) {
    			mChromosome[i] = 0;
    		} else {
    			mChromosome[i] = 1;
    		}
    	}
    }

    /**
     * Creates a number of offspring by combining (using crossover) the current
     * Gene's chromosome with another Gene's chromosome.
     * Usually two parents will produce an equal amount of offpsring, although
     * in other reproduction strategies the number of offspring produced depends
     * on the fitness of the parents.
     * @param other: the other parent we want to create offpsring from
     * @return Array of Gene offspring (default length of array is 2).
     * These offspring will need to be added to the next generation.
     */
    public Gene[] reproduce(Gene other){
        Gene[] result = new Gene[2];
        // initilization of offspring chromosome goes HERE
        
        
        // 1. select a random position, the crossover point, i E {1, ..., mChromosome.length-2}
        Random rand = new Random();
        int minPos = 1, maxPos = mChromosome.length-2;
        int crossoverPoint = rand.nextInt((maxPos - minPos) + 1) + minPos;

        // 2. cut both parents in two segments after this position
        // 3. copy the first segment of parent 1 into child 1
        // and the first segment of parent 2 into child 2
        // 4. scan parent 2 from left to right and fill the second segment of child 1 
        // with values from parent 2, skipping those that are already contained in it
        // 5. do the same for parent 1 and child 2
        Gene parent1 = this;
        Gene parent2 = other;
        Gene child1 = new Gene();
        Gene child2 = new Gene();
        for( int i=0; i < getChromosomeSize(); i++ ) {
        	
        	if( i <= crossoverPoint ) {
        		child1.setChromosomeElement(i, parent1.getChromosomeElement(i));
        		child2.setChromosomeElement(i, parent2.getChromosomeElement(i));
        	} else {
        		child1.setChromosomeElement(i, parent2.getChromosomeElement(i));
        		child2.setChromosomeElement(i, parent1.getChromosomeElement(i));
        	}
        }
        result[0] = child1;
        result[1] = child2;
        return result;
    }

    /**
     * Mutates a gene using inversion, random mutation or other methods.
     * This function is called after the mutation chance is rolled.
     * Mutation can occur (depending on the designer's wishes) to a parent
     * before reproduction takes place, an offspring at the time it is created,
     * or (more often) on a gene which will not produce any offspring afterwards.
     */
    public void mutate(){
    	Random rand = new Random();
    	
    	// let's mutate each value with independent probability p = 1 / n (n = gene length)
    	for( int i=0; i < mChromosome.length; i++ ) {
    		if( rand.nextFloat() < ((float)1 / mChromosome.length) ) {
    			if( mChromosome[i] == 1 ){
    				mChromosome[i] = 0;
    			} else {
    				mChromosome[i] = 1;
    			}
     		}
    	}
    }
    /**
     * Sets the fitness, after it is evaluated in the GeneticAlgorithm class.
     * @param value: the fitness value to be set
     */
    public void setFitness(float value) { mFitness = value; }
    /**
     * @return the gene's fitness value
     */
    public float getFitness() { return mFitness; }
    /**
     * Returns the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @return the value of the element we want to access (0 or 1)
     */
    public int getChromosomeElement(int index){ return mChromosome[index]; }

    /**
     * Sets a <b>value</b> to the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @param value: the value we want to set at position <b>index</b> of the mChromosome array (0 or 1)
     */
    public void setChromosomeElement(int index, int value){ mChromosome[index]=value; }
    /**
     * Returns the size of the chromosome (as provided in the Gene constructor)
     * @return the size of the mChromosome array
     */
    public int getChromosomeSize() { return mChromosome.length; }
    /**
     * Corresponds the chromosome encoding to the phenotype, which is a representation
     * that can be read, tested and evaluated by the main program.
     * @return a String with a length equal to the chromosome size, composed of A's
     * at the positions where the chromosome is 1 and a's at the posiitons
     * where the chromosme is 0
     */
    public String getPhenotype() {
        // create an empty string
        String result="";
        for(int i = 0; i < mChromosome.length; i++){
            // populate it with either A's or a's, depending on the the
            if(mChromosome[i]==1){
                result+= "A";
            } else {
                result+= "a";
            }
        }
        return result;
    }

    
    public int getCapitalACount() {
    	int capitalACount = 0;
    	for( int i=0; i < getChromosomeSize(); i++ ) {
    		
    		capitalACount += getChromosomeElement(i);
    	}
    	return capitalACount;
    }
    
    
	public int compare(Gene o1, Gene o2) {

		// return o1.getCapitalACount() - o2.getCapitalACount();
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

	public int compareTo(Gene o) {

		return getPhenotype().compareTo( o.getPhenotype() );
	}
    
    
    

    
    
}
