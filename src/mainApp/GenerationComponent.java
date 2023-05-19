package mainApp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;


public class GenerationComponent extends JComponent {
	
  	private final int SIDE_OFFSET = 30;
  	private final int TOP_OFFSET = 10;
  	private final int BOTTOM_OFFSET = 30;
  	
  	private final double LEGEND_X_OFFSET_RATIO = 0.2;
  	private final double LEGEND_Y_OFFSET_RATIO = 0.2;
  	private final int LEGEND_SQUARE_SIZE = 10;
  	
  	private final Color BEST_COLOR = Color.green;
  	private final Color WORST_COLOR = Color.red;
  	private final Color AVERAGE_COLOR = Color.yellow;
	private final Color DIVERSITY_COLOR = Color.BLUE;

	private final int TERMINATION_FITNESS = 100;
	private final FitnessMethod FITNESS_METHOD = FitnessMethod.DECREASING_SIGNIFICANCE;
  	
  	
  	private Generation generation;
  	private ArrayList<Byte> bestLog;
	private ArrayList<Byte> worstLog;
	private ArrayList<Byte> avgLog;
	private ArrayList<Integer> diversityLog;
	private double xInc;
	private double yInc;
	private int maxGens;
	private int genCount;
	private double diversityNormalizer;
	private double mutationRate;
  	private FitnessMethod fitnessMethod;
  	private SelectionMethod selectionMethod;
  	private int genomeLength;
  	private int populationSize;
  	private double eliteNum;
  	private boolean crossover;
  	private boolean terminated;
	
	public GenerationComponent() {
		this(100, 100, 100, 0.01, SelectionMethod.TOP_HALF, 0.0);
	}
	
	public GenerationComponent(int generationSize, int chromosomeSize, int maxGens, double mutationRate, SelectionMethod sm, double elitismPercent) {
		this((long)(Math.random() * Long.MAX_VALUE), generationSize, chromosomeSize, maxGens, mutationRate, sm, elitismPercent);
	}
	
	public GenerationComponent(long seed, int generationSize, int chromosomeSize, int maxGens, double mutationRate, SelectionMethod sm, double elitismPercent) {
  		this.setPreferredSize(new Dimension(MainApp.GENERATION_FRAME_WIDTH, MainApp.GENERATION_FRAME_HEIGHT) );
  		generation = new Generation(seed, generationSize, chromosomeSize);
  		this.maxGens = maxGens;
  		bestLog = new ArrayList<Byte>();
  		worstLog = new ArrayList<Byte>();
  		avgLog = new ArrayList<Byte>();
		diversityLog = new ArrayList<Integer>();
  		bestLog.add(generation.getBestFitness(FITNESS_METHOD));
  		worstLog.add(generation.getWorstFitness(FITNESS_METHOD));
  		avgLog.add(generation.getAvgFitness(FITNESS_METHOD));
		// diversityNormalizer = 100.0 / generation.getAvgHammingDistance();
		diversityNormalizer = 100/Math.pow(2,18);
		diversityLog.add((int) (generation.getAvgHammingDistance()*diversityNormalizer));
  		xInc = 0;
  		yInc = 0;
  		genCount = 0;
  		this.mutationRate = mutationRate;
  		selectionMethod = sm;
  		crossover = false;
  		eliteNum = generationSize * elitismPercent;
  		terminated = false;
  		populationSize = generationSize;
  		genomeLength = chromosomeSize;
  	}
  	
  	public void setNumOfGen(int size) {maxGens = size;}
  	public void setMutationRate(double rate) {mutationRate = rate;}
  	public void setFitnessMethod(FitnessMethod m) {fitnessMethod = m;}
  	public void setSelectionMethod(SelectionMethod m) {selectionMethod = m;}
  	public void setGenomeLength(int g) {genomeLength = g;}
  	public void setPopulationSize(int p) {populationSize = p;}
  	public void setEliteNum(double e) {eliteNum = e;}
  	public void setCrossover(boolean tf) {crossover = tf;}
  	public Chromosome getBestChromo() {return generation.getBestChromo();}
  	
  	@Override
  	protected void paintComponent(Graphics g) {
  		super.paintComponent(g);
  		Graphics2D g2 = (Graphics2D)g;
  		
  		
  		
  		for(int i = 0; i <= 10; i++) {
  			g2.drawString(i*10 + "", 
  					SIDE_OFFSET - 25, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10 + 5 - 1);
  			g2.drawLine(SIDE_OFFSET - 5, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10 - 1, 
  					SIDE_OFFSET, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10 - 1);
  			g2.drawString(maxGens / 10 * i + "", 
  					SIDE_OFFSET + (this.getWidth() - 2 * SIDE_OFFSET) * i / 10 - 5, 
  					this.getHeight() - BOTTOM_OFFSET + 20);
  			g2.drawLine(SIDE_OFFSET + (this.getWidth() - 2 * SIDE_OFFSET) * i / 10, 
  					this.getHeight() - BOTTOM_OFFSET, 
  					SIDE_OFFSET + (this.getWidth() - 2 * SIDE_OFFSET) * i / 10, 
  					this.getHeight() - BOTTOM_OFFSET + 5);
  		}
  		
  		
  		
  		g2.setColor(BEST_COLOR);
  		g2.fillRect((int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO),
  				LEGEND_SQUARE_SIZE, LEGEND_SQUARE_SIZE);
  		g2.setColor(Color.black);
  		g2.drawString("Best Fitness",
  				(int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO + 20), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 10));
  		
  		g2.setColor(AVERAGE_COLOR);
  		g2.fillRect((int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 20),
  				LEGEND_SQUARE_SIZE, LEGEND_SQUARE_SIZE);
  		g2.setColor(Color.black);
  		g2.drawString("Average Fitness",
  				(int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO + 20), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 10 + 20));
  		
  		g2.setColor(WORST_COLOR);
  		g2.fillRect((int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 20 * 2),
  				LEGEND_SQUARE_SIZE, LEGEND_SQUARE_SIZE);
  		g2.setColor(Color.black);
  		g2.drawString("Worst Fitness",
  				(int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO + 20), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 10 + 20 * 2));

		g2.setColor(DIVERSITY_COLOR);
		g2.fillRect((int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO), 
				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 20 * 3),
				LEGEND_SQUARE_SIZE, LEGEND_SQUARE_SIZE);
		g2.setColor(Color.black);
		g2.drawString("Diversity",
				(int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO + 20), 
				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 10 + 20 * 3));

  		
  		
  		g2.translate(SIDE_OFFSET, this.getHeight()-BOTTOM_OFFSET);
  		xInc = ((double)this.getWidth() - SIDE_OFFSET * 2) / (maxGens - 1);
  		yInc = ((double)this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) / 100;
  		
  		for(int i = 1; i <= genCount; i++) {
  			g2.setColor(BEST_COLOR);
  	  		g2.drawLine((int)(Math.round(xInc * (i - 1))), (int)(Math.round(-bestLog.get(i - 1) * yInc)), 
  	  				(int)(Math.round(xInc * i)), (int)(Math.round(-bestLog.get(i) * yInc)));
  	  			
  	  		g2.setColor(AVERAGE_COLOR);
  	  		g2.drawLine((int)(Math.round(xInc * (i - 1))), (int)(Math.round(-avgLog.get(i - 1) * yInc)), 
  	  				(int)(Math.round(xInc * i)), (int)(Math.round(-avgLog.get(i) * yInc)));
  	  			
  	  		g2.setColor(WORST_COLOR);
  			g2.drawLine((int)(Math.round(xInc * (i - 1))), (int)(Math.round(-worstLog.get(i - 1) * yInc)), 
  					(int)(Math.round(xInc * i)), (int)(Math.round(-worstLog.get(i) * yInc)));

			// Draw diversity
			g2.setColor(DIVERSITY_COLOR);
			g2.drawLine((int)(Math.round(xInc * (i - 1))), (int)(Math.round(-diversityLog.get(i - 1) * yInc)),
					(int)(Math.round(xInc * i)), (int)(Math.round(-diversityLog.get(i) * yInc)));
  		}
  		g2.translate(-SIDE_OFFSET, -this.getHeight() + BOTTOM_OFFSET);
  		
  		
  		
  		g2.setColor(Color.black);
  		g2.drawRect(SIDE_OFFSET, TOP_OFFSET - 1, this.getWidth() - 2 * SIDE_OFFSET, this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET);
  		
  	}
  	
  	public void update() {
  		if(genCount < maxGens - 1 && generation.getBestFitness(FITNESS_METHOD) < TERMINATION_FITNESS) {
  			generation.evolve(FITNESS_METHOD, selectionMethod, mutationRate, eliteNum, crossover);
  			bestLog.add(generation.getBestFitness(FITNESS_METHOD));
	  		worstLog.add(generation.getWorstFitness(FITNESS_METHOD));
	  		avgLog.add(generation.getAvgFitness(FITNESS_METHOD));
	  		diversityLog.add((int) (generation.getAvgHammingDistance()*diversityNormalizer));;
	  		genCount++;
			generation.printBestFitness(FITNESS_METHOD);
	  		// System.out.println("Mutation rate: " + mutationRate + " selection: " + selectionMethod + " crossover: " + crossover +
	  				// " population size: " + populationSize + " generations: " + maxGens + " Genome length: " + genomeLength + " Elitism: " + eliteNum);
  		} else {
  			terminated = true;
  		}
  	}
  	
  	public boolean isTerminated() {
  		return terminated;
  	}
  	
  	public SimpleChromosome[] getGeneration() {
    	return generation.getGeneration();
    }
  	
  	
}
