package mainApp;

import java.util.Arrays;

/**
 * SimpleChromosome contains all the functionality of the chromosome sans graphics
 */
public class SimpleChromosome implements Comparable<SimpleChromosome>{
    protected byte[] genes;
    private int fitness;
    private static final byte[] SMILEY_COMPARISON = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        1,0,0,1,1,0,0,1,1,1,1,0,0,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        1,0,1,1,1,1,0,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    private final int[] SPECIAL_NUMS = {0,1,2,3,4,5,6,7,8,9,10,20,30,40,50,65,80,100};

    /**
     * Default Constructor
     */
    public SimpleChromosome() {
        genes = new byte[0];
    }

    /**
     * Constructor with genes provided. Genes are deep copied.
     * @param genes
     */
    public SimpleChromosome(byte[] genes) {
        this.genes = Arrays.copyOf(genes, genes.length);
    }

    /**
     * Returns the genes of the chromosome
     * @return Genes of the chromosome
     */
    public byte[] getGenes() {
        return genes;
    }

    /**
     * Set genes to value provided
     * @param genes New genes
     */
    public void setGenes(byte[] genes) {
        this.genes = genes;
    }

    /**
	 * flips the given bit/gene of the chromosome
	 * @param n index of the bit to be flipped
     * @restrictions n must be a valid index
	 */
	public void flipBit(int n) {
		genes[n] = (byte) (genes[n] == 0 ? 1 : 0);
	}

    /**
     * Randomly mutates the chromosome with the mutation rate provided.
     * Each bit has a mutationRate chance of being flipped.
     * @param mutationRate The mutation rate for the chromosome
     */
    public void mutate(double mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            if(Math.random() < mutationRate) flipBit(i);
        }
    }

    /**
     * Sets the fitness of the chromosome to the value provided.
     * @param fitness Value of the fitness
     * @restrictions Fitness can be any integer value but values not between 0 and 100 may cause issues in other parts of the program
     */
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    /**
     * Updates the fitness of the chromosome based on the fitness method provided.
     * @param method Fitness method to use
     */
    public void setFitness(FitnessMethod method) {
        // I don't wanna brag, but this is some ELEGANT ass code
        switch(method) {
            case ONES:
                getFitnessOnes();
                break;
            case COMPARE_TO_CHROMOSOME:
                getFitnessCompareToChromosome();
                break;
            case SWITCHES:
                getFitnessSwitches();
                break;
            case DECREASING_SIGNIFICANCE:
                getFitnessDecreasingSignificance();
            case TROUGHS:
                getFitnessTroughs();
                break;
        }
    }

    /**
     * Gets the fitness of the chromosome against the ONES landscape
     */
    private void getFitnessOnes() {
        double fitness = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] == 1) fitness++;
        }
        this.fitness = (int) (100*fitness/genes.length);
    }

    /**
     * Gets the fitness of the chromosome against the COMPARE_TO_CHROMOSOME landscape (smiley face)
     */
    private void getFitnessCompareToChromosome() {
        double fitness = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] == SMILEY_COMPARISON[i]) fitness++;
        }
        this.fitness = (int) (100*fitness/genes.length);
    }

    /**
     * Gets the fitness of the chromosome against the SWITCHES landscape
     */
    private void getFitnessSwitches() {
        double fitness = 0;
        for (int i = 0; i < genes.length - 1; i++) {
            if(genes[i] != genes[i + 1]) fitness++;
        }
        this.fitness = (int) (100*fitness/genes.length);;
    }

    /**
     * Gets the fitness of the chromosome against the DECREASING_SIGNIFICANCE landscape
     */
    private void getFitnessDecreasingSignificance() {
        double fitness = 0;
        for(int i=0; i < genes.length; i++) {
            fitness += genes[i] * (genes.length - i);
        }
        this.fitness = (int) (100.0*fitness/(101*50.0));
    }

    /**
     * Gets the fitness of the chromosome against the TROUGHS landscape
     */
    private void getFitnessTroughs() {
        int setBits = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] == 1) setBits++;
        }
        int distToSpecialNum = Integer.MAX_VALUE;
        for(int specialNum : SPECIAL_NUMS) {
            if(distToSpecialNum > Math.abs(specialNum - setBits)) distToSpecialNum = (int) Math.abs(specialNum - setBits);
        }
        fitness = setBits - distToSpecialNum;
    }

    /**
     * Returns the fitness of the chromosome
     * @return Fitness of the chromosome
     */
    public int getFitness() {
        return fitness;
    }

    /**
     * Compares the fitness of this chromosome against the fitness of another for sorting purposes
     */
    @Override
    public int compareTo(SimpleChromosome other) {
        return this.getFitness() - other.getFitness();
    }
}