package mainApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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

    public void evolveLoop(FitnessMethod fitnessMethod, SelectionMethod selectionMethod, double mutationRate, double elitismNumber, int maxGenerations) {
        for(int i=0; i < maxGenerations; i++) {
            evolve(fitnessMethod, selectionMethod, mutationRate, elitismNumber);
        }
        for(SimpleChromosome chromosome : generation) {
            chromosome.setFitness(fitnessMethod);
        }
        System.out.println();
    }

    public void evolve(FitnessMethod fitnessMethod, SelectionMethod selectionMethod, double mutationRate, double elitismNumber) {
        sortGeneration(fitnessMethod);
        ArrayList<SimpleChromosome> eliteChromosomes = getElites(elitismNumber);
        ArrayList<SimpleChromosome> genSansElites = trimElites(eliteChromosomes);
        ArrayList<SimpleChromosome> bestChromosomes;
        for(SimpleChromosome chromosome : genSansElites) {
            chromosome.setFitness(fitnessMethod);
        }
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
        //bestChromosomes = crossover(bestChromosomes);
        ArrayList<SimpleChromosome> newGeneration = mutate(bestChromosomes, mutationRate);
        for(SimpleChromosome chromosome : eliteChromosomes) {
            newGeneration.add(chromosome);
        }
        generation = newGeneration.toArray(new SimpleChromosome[0]);
        printAverageFitness(fitnessMethod);
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

    private ArrayList<SimpleChromosome> mutate(ArrayList<SimpleChromosome> bestChromosomes, double mutationRate) {
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(SimpleChromosome chromosome : bestChromosomes) {
            newGeneration.add(chromosome);
            newGeneration.add(chromosome);
        }
        for(SimpleChromosome chromosome : newGeneration) {
            chromosome.mutate(mutationRate);
        }
        return newGeneration;
    }

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

    private ArrayList<SimpleChromosome> getElites(double elitismNumber) {
        ArrayList<SimpleChromosome> elites = new ArrayList<SimpleChromosome>();
        for(int i=0; i < elitismNumber; i++) {
            elites.add(generation[generation.length-i-1]);
        }
        return elites;
    }

    private ArrayList<SimpleChromosome> trimElites(ArrayList<SimpleChromosome> elites) {
        ArrayList<SimpleChromosome> newGen = new ArrayList<SimpleChromosome>(Arrays.asList(generation));
        for(SimpleChromosome elite : elites) {
            newGen.remove(elite);
        }
        return newGen;
    }


    private ArrayList<SimpleChromosome> selectTopHalf(ArrayList<SimpleChromosome> newGen) {
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i=newGen.size(); i < newGen.size()/2; i--) {
            newGeneration.add(newGen.get(i));
        }
        return newGeneration;
    }

    private ArrayList<SimpleChromosome> selectRoulette(ArrayList<SimpleChromosome> newGen) {
        ArrayList<Integer> roulette = new ArrayList<>();
        for (int i = 0; i < newGen.size(); i++) {
            for (int j = 0; j < newGen.get(i).getFitness(); j++) {
                roulette.add(i);
            }
        }
        ArrayList<SimpleChromosome> newGeneration = new ArrayList<SimpleChromosome>();
        for(int i=0; i < newGen.size()/2; i++) {
            int index = roulette.get((int) (Math.random() * roulette.size()));
            newGeneration.add(newGen.get(index));
        }
        return newGeneration;
    }

    private ArrayList<SimpleChromosome> selectRank(ArrayList<SimpleChromosome> newGen) {
        for(int i=0; i < newGen.size(); i++) {
            newGen.get(i).setFitness(i+1);
        }
        return selectRoulette(newGen);
    }

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

    private void sortGeneration(FitnessMethod method) {
        for(SimpleChromosome chromo : generation) {
            chromo.setFitness(method);
        }
        Arrays.sort(generation);
    }
}
