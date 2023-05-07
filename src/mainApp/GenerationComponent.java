package mainApp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;


public class GenerationComponent extends JComponent {
	
  	private static final int SIDE_OFFSET = 30;
  	private static final int TOP_OFFSET = 50;
  	private static final int BOTTOM_OFFSET = 50;
  	
  	private static final double LEGEND_X_OFFSET_RATIO = 0.2;
  	private static final double LEGEND_Y_OFFSET_RATIO = 0.2;
  	private static final int LEGEND_SQUARE_SIZE = 10;
  	
  	private static final int DELAY = 100;
  	
  	private static final Color BEST_COLOR = Color.green;
  	private static final Color WORST_COLOR = Color.red;
  	private static final Color AVERAGE_COLOR = Color.yellow;
  	
  	private Generation generation;
  	private int genNum;
  	
  	private int prevGenBest;
	private int prevGenWorst;
	private int prevGenAvg;
	private int xInc;
	private int yInc;
	private int genCount;
  	
  	public GenerationComponent() {
  		this.setPreferredSize(new Dimension(MainApp.GENERATION_FRAME_WIDTH, MainApp.GENERATION_FRAME_HEIGHT) );
  		generation = new Generation(42, 100, 100);
  		genNum = 100;
  		prevGenBest = generation.getBestFitness(FitnessMethod.ONES);
  		prevGenWorst = generation.getWorstFitness(FitnessMethod.ONES);
  		prevGenAvg = generation.getAvgFitness(FitnessMethod.ONES);
  		xInc = (this.getWidth() - SIDE_OFFSET * 2) / genNum;
  		yInc = (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) / 100;
  		genCount = 0;
  	}
  	
  	
  	@Override
  	protected void paintComponent(Graphics g) {
  		super.paintComponent(g);
  		Graphics2D g2 = (Graphics2D)g;
  		
  		
  		
  		g2.setColor(Color.black);
  		g2.drawRect(SIDE_OFFSET, TOP_OFFSET, this.getWidth() - 2 * SIDE_OFFSET, this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET);
  		
  		for(int i = 0; i <= 10; i++) {
  			g2.drawString(i*10 + "", 
  					SIDE_OFFSET - 25, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10 + 5);
  			g2.drawLine(SIDE_OFFSET - 5, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10, 
  					SIDE_OFFSET, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10);
  			g2.drawString(genNum / 10 * i + "", 
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
  		
  		
  		
  		g2.translate(SIDE_OFFSET, this.getHeight()-BOTTOM_OFFSET);
  		int prevGenBest = generation.getBestFitness(FitnessMethod.ONES);
  		int prevGenWorst = generation.getWorstFitness(FitnessMethod.ONES);
  		int prevGenAvg = generation.getAvgFitness(FitnessMethod.ONES);
  		int xInc = (this.getWidth() - SIDE_OFFSET * 2) / genNum;
  		int yInc = (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) / 100;
//  		for(int i = 1; i < genNum; i++) {
//  			generation.evolve(FitnessMethod.ONES, SelectionMethod.TOP_HALF, 0.001, 0);
//  			
//  			g2.setColor(BEST_COLOR);
//  			g2.drawLine(xInc * (i - 1), -prevGenBest * yInc, xInc * i, -generation.getBestFitness(FitnessMethod.ONES) * yInc);
//  			prevGenBest = generation.getBestFitness(FitnessMethod.ONES);
//  			
//  			g2.setColor(AVERAGE_COLOR);
//  			g2.drawLine(xInc * (i - 1), -prevGenAvg * yInc, xInc * i, -generation.getAvgFitness(FitnessMethod.ONES) * yInc);
//  			prevGenAvg = generation.getAvgFitness(FitnessMethod.ONES);
//  			System.out.println(prevGenAvg);
//  			g2.setColor(WORST_COLOR);
//  			g2.drawLine(xInc * (i - 1), -prevGenWorst * yInc, xInc * i, -generation.getWorstFitness(FitnessMethod.ONES) * yInc);
//  			prevGenWorst = generation.getWorstFitness(FitnessMethod.ONES);
//
//  		}
  		if(genCount < genNum) {
  			g2.translate(SIDE_OFFSET, this.getHeight()-BOTTOM_OFFSET);
  			
  			g2.setColor(BEST_COLOR);
  			g2.drawLine(xInc * (genCount - 1), -prevGenBest * yInc, xInc * genCount, -generation.getBestFitness(FitnessMethod.ONES) * yInc);
  			
  			g2.setColor(AVERAGE_COLOR);
  			g2.drawLine(xInc * (genCount - 1), -prevGenAvg * yInc, xInc * genCount, -generation.getAvgFitness(FitnessMethod.ONES) * yInc);
  			
  			g2.setColor(WORST_COLOR);
			g2.drawLine(xInc * (genCount - 1), -prevGenWorst * yInc, xInc * genCount, -generation.getWorstFitness(FitnessMethod.ONES) * yInc);
  		}
  		
  	}
  	
  	public void update() {
  		
  		if(genCount < genNum) {
  		
  			generation.evolve(FitnessMethod.ONES, SelectionMethod.TOP_HALF, 0.001, 0);
  			prevGenBest = generation.getBestFitness(FitnessMethod.ONES);
  			prevGenAvg = generation.getAvgFitness(FitnessMethod.ONES);
			prevGenWorst = generation.getWorstFitness(FitnessMethod.ONES);
		
			genCount++;
  		}
  	}
}
