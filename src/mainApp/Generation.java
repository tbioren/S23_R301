package mainApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The Generation class stores the fields and manages the actions related to
 * individual instances of generations.
 */
public class Generation {
    private SimpleChromosome[] generation;
    private int chromosomeSize;
    private long seed;
    private final int CROSSOVER_POINT = 50;

    public Generation(long seed, int size, int chromosomeSize) {
        this.seed = seed;
        generation = new SimpleChromosome[size];
        this.chromosomeSize = chromosomeSize;
        createGeneration();
    }

    public Generation(int size, int chromosomeSize) {
        this(1, size, chromosomeSize);
    }

    public Generation() {
        this(100, 100);
    }

    /**
     * Creates a new generation of chromosomes with random genes
     * @param fitnessMethod 
     * @param selectionMethod
     * @param mutationRate
     * @param elitismNumber
     * @param maxGenerations
     */
    public void evolveLoop(FitnessMethod fitnessMethod, SelectionMethod selectionMethod, double mutationRate, double elitismNumber, int maxGenerations) {
        for(int i=0; i < maxGenerations; i++) {
            evolve(fitnessMethod, selectionMethod, mutationRate, elitismNumber);
        }
        for(SimpleChromosome chromosome : generation) {
            chromosome.setFitness(fitnessMethod);
        }
        System.out.println();
    }

    /**
     * Creates a new generation of chromosomes with random genes
     */
    public void evolve(FitnessMethod fitnessMethod, SelectionMethod selectionMethod, double mutationRate, double elitismNumber) {
        sortGeneration(fitnessMethod);
        // Separate the elite chromosomes from the rest of the generation
        ArrayList<SimpleChromosome> eliteChromosomes = getElites(elitismNumber);
        ArrayList<SimpleChromosome> genSansElites = trimElites(eliteChromosomes);
        ArrayList<SimpleChromosome> bestChromosomes;
        for(SimpleChromosome chromosome : genSansElites) {
            chromosome.setFitness(fitnessMethod);
        }
        // Select the best chromosomes from the generation with the given selection method
        switch(selectionMethod) {
            default:
                bestChromosomes = selectTopHalf(genSansElites);
                break;
            case ROULETTE:
                bestChromosomes = selectRoulette(genSansElites);
                break;
            case RANK:
                bestChromosomes = selectRank(genSansElites);
                break;
        }
        //bestChromosomes = crossover(bestChromosomes); // Crossover the best chromosomes LEAVE COMMENTED FOR M2
        ArrayList<SimpleChromosome> newGeneration = mutate(bestChromosomes, mutationRate);

        // Since you cant have half a chromosome, if the elitism number is odd, remove the first chromosome (the worst one)
        if (elitismNumber % 2 != 0){
            newGeneration.remove(0);
        }

        // Add the elite chromosomes back into the generation
        for(SimpleChromosome chromosome : eliteChromosomes) {
            newGeneration.add(new SimpleChromosome(chromosome.getGenes()));
        }
        generation = newGeneration.toArray(new SimpleChromosome[0]);
        printAverageFitness(fitnessMethod);
        // Don't remove the comment on the next line
        //System.out.prntln();
    }

    public void printAverageFitness(FitnessMethod fitnessMethod) {
        int totalFitness = 0;
        for(SimpleChromosome chromosome : generation) {
            chromosome.setFitness(fitnessMethod);
            totalFitness += chromosome.getFitness();
        }
        System.out.println("Average fitness: " + totalFitness/generation.length);
    }

    // Creates 2 mutant children from each parent
    private ArrayList<SimpleChromosome> mutate(ArrayList<SimpleChromosome> bestChromosomes, double mutationRate) {
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i = 0; i < bestChromosomes.size(); i++) {
            newGeneration.add(new SimpleChromosome(bestChromosomes.get(i).getGenes()));
            newGeneration.add(new SimpleChromosome(bestChromosomes.get(i).getGenes()));
        }
        for(SimpleChromosome chromosome : newGeneration) {
            chromosome.mutate(mutationRate);
        }
        return newGeneration;
    }

    // Crossover. It's complicated so look at the technical documentation if you're big enough of a nerd to care
    private ArrayList<SimpleChromosome> crossover(ArrayList<SimpleChromosome> bestChromosomes) {
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i=0; i < bestChromosomes.size(); i+=2) {
            byte[] parent1Start = Arrays.copyOfRange(bestChromosomes.get(i).getGenes(), 0, CROSSOVER_POINT);
            byte[] parent1End = Arrays.copyOfRange(bestChromosomes.get(i).getGenes(), CROSSOVER_POINT, chromosomeSize);
            byte[] parent2Start = Arrays.copyOfRange(bestChromosomes.get(i+1).getGenes(), 0, CROSSOVER_POINT);
            byte[] parent2End = Arrays.copyOfRange(bestChromosomes.get(i+1).getGenes(), CROSSOVER_POINT, chromosomeSize);
            byte[] child1 = new byte[chromosomeSize];
            byte[] child2 = new byte[chromosomeSize];
            for(int j=0; j < parent1Start.length; j++) {
                child1[j] = parent1Start[j];
                child2[j] = parent2Start[j];
            }
            for(int j=0; j < parent1End.length; j++) {
                child1[j+CROSSOVER_POINT] = parent1End[j];
                child2[j+CROSSOVER_POINT] = parent2End[j];
            }
            newGeneration.add(new SimpleChromosome(child1));
            newGeneration.add(new SimpleChromosome(child2));
        }
        return newGeneration;
    }

    // Selects the best chromosomes from the generation to pass straight through to the next generation
    private ArrayList<SimpleChromosome> getElites(double elitismNumber) {
        ArrayList<SimpleChromosome> elites = new ArrayList<SimpleChromosome>();
        for(int i=0; i < elitismNumber; i++) {
            elites.add(generation[generation.length-i-1]);
        }
        return elites;
    }

    private ArrayList<SimpleChromosome> trimElites(ArrayList<SimpleChromosome> elites) {
        ArrayList<SimpleChromosome> newGen = new ArrayList<SimpleChromosome>();

        for (int i = 0; i < generation.length - elites.size(); i++) {
            newGen.add(new SimpleChromosome(generation[i].getGenes()));
        }

        return newGen;
    }

    // Selects the top 1/2 of the generation to pass through to the next generation
    private ArrayList<SimpleChromosome> selectTopHalf(ArrayList<SimpleChromosome> newGen) {
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i=newGen.size()/2; i < newGen.size(); i++) {
            byte[] genes = Arrays.copyOf(newGen.get(i).getGenes(), newGen.get(i).getGenes().length);
            newGeneration.add(new SimpleChromosome(genes));
        }
        return newGeneration;
    }

    // Selects the chromosomes from the generation with the given selection method (I'm not sure if this works)
    private ArrayList<SimpleChromosome> selectRoulette(ArrayList<SimpleChromosome> newGen) {
        ArrayList<Integer> roulette = new ArrayList<>();
        for (int i = 0; i < newGen.size(); i++) {
            for (int j = 0; j < newGen.get(i).getFitness(); j++) {
                roulette.add(i);
            }
        }
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i=0; i < newGen.size()/2+1; i++) {
            int index = roulette.get((int) (Math.random() * roulette.size()));
            byte[] genes = Arrays.copyOf(newGen.get(index).getGenes(), newGen.get(index).getGenes().length);
            newGeneration.add(new SimpleChromosome(genes));
        }
        return newGeneration;
    }

    // Selects the chromosomes from the generation with the given selection method (I'm not sure if this works)
    private ArrayList<SimpleChromosome> selectRank(ArrayList<SimpleChromosome> newGen) {
        for(int i=0; i < newGen.size(); i++) {
            newGen.get(i).setFitness(i+1);
        }
        return selectRoulette(newGen);
    }

    // Fills the genome of each chromosome with seeded random values
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

    // Sorts the generation by fitness {least fit, ..., most fit}
    private void sortGeneration(FitnessMethod method) {
        for(SimpleChromosome chromo : generation) {
            chromo.setFitness(method);
        }
        Arrays.sort(generation);
    }
}