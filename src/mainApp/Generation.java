package mainApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Contains a generation of SimpleChromosomes. Manages evolution.
 */
public class Generation {
    private SimpleChromosome[] generation;
    private int chromosomeSize;
    private long seed;
    private Chromosome bestChromo;

    /**
     * Constructor with seed, size, and chromosome size
     * @param seed
     * @param size
     * @param chromosomeSize
     */
    public Generation(long seed, int size, int chromosomeSize) {
        this.seed = seed;
        generation = new SimpleChromosome[size];
        this.chromosomeSize = chromosomeSize;
        createGeneration();
    }

    /**
     * Constructor with size and chromosome size
     * @param size
     * @param chromosomeSize
     */
    public Generation(int size, int chromosomeSize) {
        this(1, size, chromosomeSize);
    }

    /**
     * Default Constructor
     */
    public Generation() {
        this(100, 1000);
    }
    
    /**
     * Returns the fittest chromosome
     * @return Fittest Chromosome
     */
    public Chromosome getBestChromo() {
    	return bestChromo;
    }
    
    /**
     * Returns the current generation
     * @return SimpleChromosome[] generation
     */
    public SimpleChromosome[] getGeneration() {
    	return generation;
    }
    

    /**
     * This is the workhorse method. Creates a new generation with varous parameters.
     * @param fitnessMethod Fitness method to use
     * @param selectionMethod Selection method to use
     * @param mutationRate Mutation rate of non-elite chromosomes
     * @param elitismNumber Percent of the top chromosomes to be passed directly to next generation
     * @param crossover Use/don't use crossover
     */
    public void evolve(FitnessMethod fitnessMethod, SelectionMethod selectionMethod, double mutationRate, double elitismNumber, boolean crossover) {
        sortGeneration(fitnessMethod);
        getBestFitness(fitnessMethod);
        
        // Separate the elite chromosomes from the rest of the generation
        ArrayList<SimpleChromosome> eliteChromosomes = getElites(elitismNumber);
        ArrayList<SimpleChromosome> genSansElites = trimElites(eliteChromosomes);
        ArrayList<SimpleChromosome> bestChromosomes;
        for(SimpleChromosome chromosome : genSansElites) {
            chromosome.setFitness(fitnessMethod);
        }
        // Select the best chromosomes from the generation with the given selection method
        switch(selectionMethod) {
            case ROULETTE:
                bestChromosomes = selectRoulette(genSansElites);
                break;
            case RANK:
                bestChromosomes = selectRank(genSansElites);
                break;
            case TRUNCATION:
                bestChromosomes = selectTruncation(genSansElites);
                break;
            case BEST_RANDOM_WORST: 
                bestChromosomes = selectBestRandomWorst(genSansElites);
                break;
            default:
                bestChromosomes = selectTruncation(genSansElites);
                break;
        }
        // Crossover if selected
        if(crossover) bestChromosomes = crossover(bestChromosomes);
        
        // Mutate the non-elites
        ArrayList<SimpleChromosome> newGeneration = mutate(bestChromosomes, mutationRate, genSansElites.size() % 2 == 1);

        // Since you cant have half a chromosome, if the elitism number is odd, remove the first chromosome (the worst one)
        if (elitismNumber % 2 != 0){
            newGeneration.remove(0);
        }

        // Add the elite chromosomes back into the generation
        for(SimpleChromosome chromosome : eliteChromosomes) {
            newGeneration.add(new SimpleChromosome(chromosome.getGenes()));
        }

        // Copy the new generation to generation[]
        generation = newGeneration.toArray(new SimpleChromosome[0]);
    }

    /**
     * Prints the average fitness of the generation
     * @param fitnessMethod Fitness method to evaluate the chromosomes' fitness against
     */
    public void printAverageFitness(FitnessMethod fitnessMethod) {
        int totalFitness = 0;
        for(SimpleChromosome chromosome : generation) {
            chromosome.setFitness(fitnessMethod);
            totalFitness += chromosome.getFitness();
        }
        System.out.println("Average fitness: " + totalFitness/generation.length);
    }

    /**
     * Prints the fitness of the fittest chromosome
     * @param fitnessMethod Fitness method to evaluate the chomosomes' fitness against
     */
    public void printBestFitness(FitnessMethod fitnessMethod) {
        System.out.println("Best fitness: " + getBestFitness(fitnessMethod));
    }

    /**
     * Mutates the SimpleChromosomes in the given ArrayList
     * @param bestChromosomes SimpleChromosomes to mutate
     * @param mutationRate Mutation rate
     * @param isOdd True if the size of the generation is odd (not bestChromosomes)
     * @return Mutated SimpleChromosomes
     */
    private ArrayList<SimpleChromosome> mutate(ArrayList<SimpleChromosome> bestChromosomes, double mutationRate, boolean isOdd) {
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i = 0; i < bestChromosomes.size(); i++) {
            newGeneration.add(new SimpleChromosome(bestChromosomes.get(i).getGenes()));
            if(!(isOdd && i == bestChromosomes.size() - 1))
            	newGeneration.add(new SimpleChromosome(bestChromosomes.get(i).getGenes()));
        }
        for(SimpleChromosome chromosome : newGeneration) {
            chromosome.mutate(mutationRate);
        }
        return newGeneration;
    }

    /**
     * Crosses genes of parent chromosomes to children
     * @param bestChromosomes Parents
     * @return Children
     */
    private ArrayList<SimpleChromosome> crossover(ArrayList<SimpleChromosome> bestChromosomes) {
        int crossoverPoint = bestChromosomes.size() / 2;
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        // If the length of newGeneration is odd, the last chromosome will be put directly thru because it can't have a partner
        if(bestChromosomes.size() % 2 != 0) newGeneration.add(bestChromosomes.get(bestChromosomes.size() - 1));
        // Take all of the parents and cross them over. (It looks gross but it's just grabbing the first and second halves of the
        // parents and splicing them together).
        for(int i=0; i < bestChromosomes.size() - 1; i+=2) {
            byte[] parent1Start = Arrays.copyOfRange(bestChromosomes.get(i).getGenes(), 0, crossoverPoint);
            byte[] parent1End = Arrays.copyOfRange(bestChromosomes.get(i).getGenes(), crossoverPoint, chromosomeSize);
            byte[] parent2Start = Arrays.copyOfRange(bestChromosomes.get(i+1).getGenes(), 0, crossoverPoint);
            byte[] parent2End = Arrays.copyOfRange(bestChromosomes.get(i+1).getGenes(), crossoverPoint, chromosomeSize);
            byte[] child1 = new byte[chromosomeSize];
            byte[] child2 = new byte[chromosomeSize];
            for(int j=0; j < parent1Start.length; j++) {
                child1[j] = parent1Start[j];
                child2[j] = parent2Start[j];
            }
            for(int j=0; j < parent1End.length; j++) {
                child1[j+crossoverPoint] = parent1End[j];
                child2[j+crossoverPoint] = parent2End[j];
            }
            newGeneration.add(new SimpleChromosome(child1));
            newGeneration.add(new SimpleChromosome(child2));
        }
        return newGeneration;
    }

    /**
     * Returns the elitismNumber fittest chromosomes
     * @param elitismNumber Number of fittest SimpleChromosmoes to return
     * @return elitismNumber fittest SimpleChromosomes
     */
    private ArrayList<SimpleChromosome> getElites(double elitismNumber) {
        ArrayList<SimpleChromosome> elites = new ArrayList<SimpleChromosome>();
        for(int i=0; i < elitismNumber; i++) {
            elites.add(generation[generation.length-i-1]);
        }
        return elites;
    }

    /**
     * Returns an ArrayList that is made up of generation[] without the elite chromosomes
     * @param elites Elite chromosomes to be excluded
     * @return ArrayList of SimpleChromosomes without the elite chromosomes
     */
    private ArrayList<SimpleChromosome> trimElites(ArrayList<SimpleChromosome> elites) {
        ArrayList<SimpleChromosome> newGen = new ArrayList<SimpleChromosome>();

        for (int i = 0; i < generation.length - elites.size(); i++) {
            newGen.add(new SimpleChromosome(generation[i].getGenes()));
        }

        return newGen;
    }

    /**
     * Selects the top 50% of the generation to pass on
     * @param newGen Chromosomes to choose from
     * @return The best 50% of the generation
     */
    private ArrayList<SimpleChromosome> selectTruncation(ArrayList<SimpleChromosome> newGen) {
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i=(newGen.size()+1)/2; i < newGen.size(); i++) {
            byte[] genes = Arrays.copyOf(newGen.get(i).getGenes(), newGen.get(i).getGenes().length);
            newGeneration.add(new SimpleChromosome(genes));
        }
        return newGeneration;
    }

    /**
     * Roulette Selection
     * @param newGen Chromosomes to choose from
     * @return The chromosomes selected via roulette selection
     */
    private ArrayList<SimpleChromosome> selectRoulette(ArrayList<SimpleChromosome> newGen) {
        // Sets up the roulette
        ArrayList<Integer> roulette = new ArrayList<Integer>();
        for (int i = 0; i < newGen.size(); i++) {
            for (int j = 0; j < newGen.get(i).getFitness(); j++) {
                roulette.add(i);
            }
        }
        // Selects the chromosomes
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i=0; i < (newGen.size()+1)/2; i++) {
            int index = roulette.get((int) (Math.random() * roulette.size()));
            byte[] genes = Arrays.copyOf(newGen.get(index).getGenes(), newGen.get(index).getGenes().length);
            newGeneration.add(new SimpleChromosome(genes));
        }
        return newGeneration;
    }

    /**
     * Rank Selection
     * @param newGen Chromosomes to choose from
     * @return The chromosomes selected via rank selection
     */
    private ArrayList<SimpleChromosome> selectRank(ArrayList<SimpleChromosome> newGen) {
        // Set each chromosome's fitness to its rank (so you can use roulette selection)
        for(int i=0; i < newGen.size(); i++) {
            newGen.get(i).setFitness(i+1);
        }
        return selectRoulette(newGen);
    }

    /**
     * BestRandomWorst Selection
     * @param newGen Chromosomes to choose from
     * @return The chromosomes selected via BestRandomWorst selection
     */
    public ArrayList<SimpleChromosome> selectBestRandomWorst(ArrayList<SimpleChromosome> newGen) {
        ArrayList<SimpleChromosome> nextGen = new ArrayList<SimpleChromosome>();
        // Select worst chromosomes
        for(int i=0; i < newGen.size()/8; i++) {
            byte[] genes = Arrays.copyOf(newGen.get(i).getGenes(), newGen.get(i).getGenes().length);
            nextGen.add(new SimpleChromosome(genes));
        }
        // Select best chromosomes
        for(int i=0; i < newGen.size()/4; i++) {
            byte[] genes = Arrays.copyOf(newGen.get(newGen.size() - i-1).getGenes(), newGen.get(newGen.size() - i-1).getGenes().length);
            nextGen.add(new SimpleChromosome(genes));
        }
        // Select random chromosomes
        while(nextGen.size() < newGen.size()/2) {
            nextGen.add(newGen.get((int) (Math.random() * newGen.size())));
        }
        return nextGen;
    }

    /**
     * Fills generation[] with random chromosomes
     */
    private void createGeneration() {
        Random rand = new Random(seed);
        for (int i = 0; i < generation.length; i++) {
            byte[] tempGenes = new byte[chromosomeSize];
            for(int j=0; j < tempGenes.length; j++) {
                tempGenes[j] = rand.nextBoolean() ? (byte) 1 : (byte) 0;
            }
            generation[i] = new SimpleChromosome(tempGenes);
        }
    }

    /**
     * Sorts the generation by fitness {least fit, ..., most fit}
     * @param method Fitness method to use
     */
    private void sortGeneration(FitnessMethod method) {
        for(SimpleChromosome chromo : generation) {
            chromo.setFitness(method);
        }
        Arrays.sort(generation);
    }

    /**
     * Returns the fitness of the fittest chromosome
     * @param fitnessMethod Fitness method to use
     * @return Fitness of the fittest chromosome
     */
    public byte getBestFitness(FitnessMethod fitnessMethod) {
    	sortGeneration(fitnessMethod);
    	bestChromo = new Chromosome(generation[generation.length - 1]);
    	return (byte)generation[generation.length - 1].getFitness();
    }

    /**
     * Returns the fitness of the least fit chromosome
     * @param fitnessMethod Fitness method to use
     * @return Fitness of the least fit chromosome
     */
    public byte getWorstFitness(FitnessMethod fitnessMethod) {
    	sortGeneration(fitnessMethod);
    	return (byte)generation[0].getFitness();
    }

    /**
     * Returns the average fitness of the generation
     * @param fitnessMethod Fitness method to use
     * @return Average fitness of the generation
     */
    public byte getAvgFitness(FitnessMethod fitnessMethod) {
    	int sum = 0;
    	sortGeneration(fitnessMethod);
    	for(SimpleChromosome chromo : generation) {
    		sum += chromo.getFitness();
    	}
    	return (byte)(sum / generation.length);
    }

    /**
     * Returns the average Hamming distance of the generation. Hamming distance is a measure of diversity.
     * @return Average Hamming distance of the generation
     */
    public int getAvgHammingDistance() {
        int sum = 0;
        for(int i=0; i < generation.length; i++) {
            int zero = 0, one = 0;
            for(int j=0; j < generation[i].getGenes().length; j++) {
                if(generation[i].getGenes()[j] == 0) zero ++;
                else one ++;
            }
            sum += zero*one;
        }
        return sum / generation.length;
    }
}