package mainApp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * Manages the graphics of showing the whole population as it evolves
 * by presenting it in a square matrix.
 *
 */
public class PopulationComponent extends JComponent{
	private byte[][][] matrix;
	private final double BORDER_TO_BOX_RATIO = 1.0 / 10;
	private final Color COLOR_0 = Color.black;
	private final Color COLOR_1 = Color.green;
	private int popSize;
	private int boxWidth;
	private int borderWidth;
	private int pixelWidth;
	private int matrixLength;
	
	public PopulationComponent(SimpleChromosome[] g) {
		this.popSize = g.length;
		//finding the smallest matrix size to include all the chromosomes in a square
		for(int i = 0; i < Math.sqrt(Integer.MAX_VALUE); i++) {
			if(i * i == popSize || i * i > popSize) {
				matrixLength = i;
				break;
			} 
		}
		
		int count = popSize;
		try {
			this.matrix = new byte[matrixLength][matrixLength][g[0].getGenes().length];
		} catch(IndexOutOfBoundsException e) {
			return;
		}
		//assigning the genes of each chromosome to the matrix
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(count > 0) {
					matrix[i][j] = g[i * matrixLength + j].getGenes();
					count--;
				} else {
					for(int k = 0; k < matrix[i][j].length; k++) {
						matrix[i][j][k] = 3;
					}
				}
			}
		}
	}
	
	/**
	 * Sets the displayed generation to the new evolved generation
	 * 
	 * @param g
	 */
	public void setGeneration(SimpleChromosome[] g) {
		this.popSize = g.length;
		for(int i = 0; i < Math.sqrt(Integer.MAX_VALUE); i++) {
			if(i * i == popSize || i * i > popSize) {
				matrixLength = i;
				break;
			} 
		}
		int count = popSize;
		try {
			this.matrix = new byte[matrixLength][matrixLength][g[0].getGenes().length];
		} catch(IndexOutOfBoundsException e) {
			return;
		}
		//assigning the genes of each chromosome to the matrix
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(count > 0) {
					matrix[i][j] = g[i * matrixLength + j].getGenes();
					count--;
				} else {
					for(int k = 0; k < matrix[i][j].length; k++) {
						matrix[i][j][k] = 3;
					}
				}
			}
		}
	}
	
	/**
	 *Paints the generation in a square matrix. Each chromosome is presented as a x by 10 rectangle.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//calculate the length of each chromosome and their distance from each other from the window size
		double horBoxLength = this.getWidth() / (matrix.length + (matrix.length + 1.0) * BORDER_TO_BOX_RATIO);
		double verBoxLength = this.getHeight() / (matrix.length + (matrix.length + 1.0) * BORDER_TO_BOX_RATIO);
		double bestLength = horBoxLength < verBoxLength ? horBoxLength : verBoxLength;
		boxWidth = (int)(Math.round(bestLength));
		borderWidth = (int)(Math.round(bestLength * BORDER_TO_BOX_RATIO));
		pixelWidth = borderWidth;
		
		g2.translate(borderWidth, borderWidth);
		//loops through each chromosome of the generation
		for(int i = 0; i < matrix.length; i++) {
			g2.translate(0, (borderWidth + boxWidth) * i);
			for(int j = 0; j < matrix[i].length; j++) {
				g2.translate((borderWidth + boxWidth) * j, 0);
				//loops through the genes of each chromosome
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
