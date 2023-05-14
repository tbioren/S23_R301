package mainApp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class PopulationComponent extends JComponent{
	private SimpleChromosome[] generation;
	private byte[][][] matrix;
	private final double BOX_RATIO = 10 / 122;
	private final double BORDER_RATIO = 2 / 122;
	private final Color COLOR_0 = Color.black;
	private final Color COLOR_1 = Color.green;
	
	
	public PopulationComponent(SimpleChromosome[] g) {
		this.generation = g;
		this.matrix = new byte[10][g.length/10][100];
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < g.length/10; j++) {
				matrix[i][j] = g[i * 10 + j].getGenes();
			}
		}
	}
	
	public void setGeneration(SimpleChromosome[] g) {
		this.generation = g;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < g.length/10; j++) {
				matrix[i][j] = g[i * 10 + j].getGenes();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//g2.translate((int)(this.getWidth() * BORDER_RATIO), (int)(this.getWidth() * BORDER_RATIO));
		g2.translate(10, 10);
		for(int i = 0; i < 10; i++) {
			g2.translate(0, 60 * i);
			for(int j = 0; j < 10; j++) {
				//g2.translate((int)(this.getWidth() * BOX_RATIO + this.getWidth() * BORDER_RATIO) * j,0);
				g2.translate(60 * j, 0);
				g2.fillRect(0, 0, 10, 10);
				for(int r = 0; r < 10; r++) {
					for(int c = 0; c < 10; c++) {
						if(matrix[i][j][(r+1)*(c+1) - 1] == 0) 
							g2.setColor(COLOR_0);
						else
							g2.setColor(COLOR_1);
//						g2.fillRect(r * (int)(1/10 * this.getWidth() * BOX_RATIO), c * (int)(1/10 * this.getWidth() * BOX_RATIO),
//								(int)(1/10 * this.getWidth() * BOX_RATIO), (int)(1/10 * this.getWidth() * BOX_RATIO));
						g2.fillRect(r * 5, c * 5, 5, 5);
					}
				}
				//translate(-(int)(this.getWidth() * BOX_RATIO + this.getWidth() * BORDER_RATIO) * j,0);
				g2.translate(-60 * j, 0);
				
				
			}
			g2.translate(0, -60 * i);
			//g2.translate(0, (int)(this.getWidth() * BOX_RATIO + this.getWidth() * BORDER_RATIO));
		}
		g2.translate(-10, -10);
	}
	
}
