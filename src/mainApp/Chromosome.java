package mainApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

/**this should be the good version
 * @author S23_R301
 * 
 * The Chromosome class stores the fields and manages the actions related to 
 * individual instances of chromosomes.
 *
 */
public class Chromosome extends SimpleChromosome{
	private File geneFile;
	private Color COLOR_0 = Color.BLACK;
	private Color COLOR_1 = Color.GREEN;
	private final double GENE_PADDING = 0.1;
	private ArrayList<Rectangle> geneRectangles;
	
	public Chromosome() throws IOException, IncorrectFileSizeException {
		super();
		geneFile = null;
		geneRectangles = new ArrayList<Rectangle>();
	}

	public Chromosome(String path) throws IOException, IncorrectFileSizeException {
		this(new File(path));
	}
	
	public Chromosome(File f) throws IOException, IncorrectFileSizeException {
		geneFile = f;
		geneRectangles = new ArrayList<Rectangle>();
		readToGenes(geneFile);
	}
	
	public Chromosome(SimpleChromosome c) {
		this.setGenes(c.getGenes());
		geneFile = null;
		geneRectangles = new ArrayList<Rectangle>();
	}
	
	/**
	 * Reads the given file into the array genes; throws IncorrectFileSizeException if
	 * the format of the file is different than required (10x10 or 2x10)
	 * 
	 * @param f
	 * @throws IOException
	 * @throws IncorrectFileSizeException
	 */
	public void readToGenes(File f) throws IOException, IncorrectFileSizeException {
		Scanner sc = new Scanner(f);
		if(!sc.hasNext()) {
			genes = new byte[0];
			sc.close();
			return;
		}
		String[] s = sc.nextLine().split(",");
		if(s.length != 100 && s.length != 20) {
			sc.close();
			throw new IncorrectFileSizeException();
		}
		genes = new byte[s.length];
		for(int i = 0; i < s.length; i++) {
			genes[i] = Byte.parseByte(s[i]);
		}
		sc.close();
	}
	
	/**
	 * Writes the current chromosome into a user-selected file
	 * 
	 * @throws IOException
	 */
	public void writeToFile() throws IOException {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Specify a file to save");   
		int userSelection = fc.showSaveDialog(null);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fc.getSelectedFile();
			System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}
		FileWriter fw = new FileWriter(fc.getSelectedFile());
		for(int i=0; i < genes.length; i++) {
			fw.write(genes[i] + ",");
		}
		fw.close();
	}
	
	/**
	 * Draws the genes of the chromosome into a 10x10 or 2x10 grid
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param g
	 */
	public void drawOn(int x, int y, int width, int height, Graphics2D g) {
		int geneSize = 0;
		if(width > height) geneSize = height / 10;
		else geneSize = width / 10;
		geneRectangles.clear();
		for(int i=0; i < genes.length; i++) {
			Rectangle r = new Rectangle((int) ((geneSize) * (i%10)),(int) ((geneSize) * (i/10)), (int) ((1-GENE_PADDING)*geneSize), (int) ((1-GENE_PADDING)*geneSize));
			geneRectangles.add(r);
		}
		for(int i=0; i < genes.length; i++) {
			g.setColor(genes[i] == 0 ? COLOR_0 : COLOR_1);
			g.fill(geneRectangles.get(i));
		}
	}

	/**
	 * handles an individual gene being clicked
	 * @param p
	 */
	public void handleClick(Point p) {
		for(Rectangle r : geneRectangles) {
			if(r.contains(p)) {
				flipBit(geneRectangles.indexOf(r));
			}
		}
	}

	/**
	 * Get the file name
	 * @return
	 */
	public String getFileName() {
		if (geneFile == null) return "No file selected";
		return geneFile.getName();
	}
	
	/**
	 * Set the file name
	 * @param f
	 * @throws IOException
	 * @throws IncorrectFileSizeException
	 */
	public void setFile(File f) throws IOException, IncorrectFileSizeException {
		geneFile = f;
		readToGenes(geneFile);
	}
	
}
