package mainApp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class PopulationComponent extends JComponent{
	private SimpleChromosome[] generation;
	private byte[][][] matrix;
	private final double BORDER_TO_BOX_RATIO = 1.0 / 10;
	private final Color COLOR_0 = Color.black;
	private final Color COLOR_1 = Color.green;
	private int popSize;
	private int boxWidth;
	private int borderWidth;
	private int pixelWidth;
	
	public PopulationComponent(SimpleChromosome[] g) {
		this.generation = g;
		this.popSize = g.length;
		int count = popSize;
		try {
			this.matrix = new byte[10][popSize % 10 == 0 ? popSize/10 : popSize/10+1][g[0].getGenes().length];
		} catch(IndexOutOfBoundsException e) {
			return;
		}
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(count > 0) {
					matrix[i][j] = g[i * 10 + j].getGenes();
					count--;
				} else {
					for(int k = 0; k < matrix[i][j].length; k++) {
						matrix[i][j][k] = 3;
					}
				}
			}
		}
	}
	
	public void setGeneration(SimpleChromosome[] g) {
		this.generation = g;
		this.popSize = g.length;
		int count = popSize;
		try {
			this.matrix = new byte[popSize % 10 == 0 ? popSize/10 : popSize/10+1][10][g[0].getGenes().length];
		} catch(IndexOutOfBoundsException e) {
			return;
		}
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(count > 0) {
					matrix[i][j] = g[i * 10 + j].getGenes();
					count--;
				} else {
					for(int k = 0; k < matrix[i][j].length; k++) {
						matrix[i][j][k] = 3;
					}
				}
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		double horBoxLength = this.getWidth() / (10.0 + 11 * BORDER_TO_BOX_RATIO);
		double verBoxLength = this.getHeight() / (matrix.length + (matrix.length + 1.0) * BORDER_TO_BOX_RATIO);
		double bestLength = horBoxLength < verBoxLength ? horBoxLength : verBoxLength;
		boxWidth = (int)(Math.round(bestLength));
		borderWidth = (int)(Math.round(bestLength * BORDER_TO_BOX_RATIO));
		pixelWidth = borderWidth;
		g2.translate(borderWidth, borderWidth);
		for(int i = 0; i < matrix.length; i++) {
			g2.translate(0, (borderWidth + boxWidth) * i);
			for(int j = 0; j < matrix[i].length; j++) {
				g2.translate((borderWidth + boxWidth) * j, 0);
				for(int r = 0; r < (matrix[i][j].length % 10 == 0 ? matrix[i][j].length / 10 : matrix[i][j].length / 10 + 1); r++) {
					for(int c = 0; c < 10; c++) {
						try {
							if(matrix[i][j][r * 10 +  c] == 0) {
								g2.setColor(COLOR_0);
								g2.fillRect(c * pixelWidth, r * pixelWidth, pixelWidth, pixelWidth);
							}
							else if(matrix[i][j][r * 10 + c] == 1) {
								g2.setColor(COLOR_1);
								g2.fillRect(c * pixelWidth, r * pixelWidth, pixelWidth, pixelWidth);
							}
						} catch(IndexOutOfBoundsException e) {
							
						}
					}
				}
				g2.translate(-(borderWidth + boxWidth) * j, 0);
			}
			g2.translate(0, -(borderWidth + boxWidth) * i);
		}
		g2.translate(-borderWidth, -borderWidth);

	}
	
}
