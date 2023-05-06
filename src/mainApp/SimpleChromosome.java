package mainApp;

public class SimpleChromosome implements Comparable<SimpleChromosome>{
    byte[] genes;
    int fitness;

    public SimpleChromosome() {
        genes = new byte[0];
    }

    public SimpleChromosome(byte[] genes) {
        this.genes = genes;
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
        }
    }

    private void getFitnessOnes() {
        int fitness = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] == 1) fitness++;
        }
        this.fitness = fitness;
    }

    private void getFitnessCompareToChromosome() {
        int fitness = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] == 1) fitness++;
        }
        this.fitness = fitness;
    }

    private void getFitnessSwitches() {
        int fitness = 0;
        for (int i = 0; i < genes.length - 1; i++) {
            if(genes[i] != genes[i + 1]) fitness++;
        }
        this.fitness = fitness;
    }

    public int getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(SimpleChromosome other) {
        return this.getFitness() - other.getFitness();
    }
}