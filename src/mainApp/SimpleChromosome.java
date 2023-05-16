package mainApp;

import java.util.Arrays;

public class SimpleChromosome implements Comparable<SimpleChromosome>{
    protected byte[] genes;
    private int fitness;
    private final byte[] SMILEY_COMPARISON = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        1,0,0,1,1,0,0,1,1,1,1,0,0,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
        1,0,1,1,1,1,0,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    private final int[] SPECIAL_NUMS = {1,2,4,8,16,32,64,128,256,512,1024};

    public SimpleChromosome() {
        genes = new byte[0];
    }

    public SimpleChromosome(byte[] genes) {
        this.genes = Arrays.copyOf(genes, genes.length);
    }

    public byte[] getGenes() {
        return genes;
    }

    public void setGenes(byte[] genes) {
        this.genes = genes;
    }

    /**
	 * flips the given bit/gene of the chromosome
	 * @param n
	 */
	public void flipBit(int n) {
		genes[n] = (byte) (genes[n] == 0 ? 1 : 0);
	}

    public void mutate(double mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            if(Math.random() < mutationRate) flipBit(i);
        }
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    // I don't wanna brag, but this is some ELEGANT ass code
    public void setFitness(FitnessMethod method) {
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
            case TROUGHS:
                getFitnessTroughs();
                break;
        }
    }

    private void getFitnessOnes() {
        double fitness = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] == 1) fitness++;
        }
        this.fitness = (int) (100*fitness/genes.length);
    }

    private void getFitnessCompareToChromosome() {
        double fitness = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] == SMILEY_COMPARISON[i]) fitness++;
        }
        this.fitness = (int) (100*fitness/genes.length);
    }

    private void getFitnessSwitches() {
        double fitness = 0;
        for (int i = 0; i < genes.length - 1; i++) {
            if(genes[i] != genes[i + 1]) fitness++;
        }
        this.fitness = (int) (100*fitness/genes.length);;
    }

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

    public int getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(SimpleChromosome other) {
        return this.getFitness() - other.getFitness();
    }
}