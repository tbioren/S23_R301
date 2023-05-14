package mainApp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
//this should be the good version
public class ChromosomeComponent extends JComponent{
	
    private Chromosome chromo;
    private double mutationRate;

    public ChromosomeComponent() throws IOException, IncorrectFileSizeException {
        chromo = new Chromosome();
        mutationRate = 0.0;
        addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				chromo.handleClick(e.getPoint());
                repaint();
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
    }
    
    public ChromosomeComponent(Chromosome chromo) {
    	this.chromo = chromo;
    	mutationRate = 0.0;
        addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				chromo.handleClick(e.getPoint());
                repaint();
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
    }
    
    public void setChromo(Chromosome chromo) {
    	this.chromo = chromo;
    }

    /**
     * Paints the current chromosome
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chromo.drawOn(0,0, getWidth(), getHeight(), (Graphics2D) g);
    }

    /**
     * returns file name for the current chromosome
     * @return chromo.getFileName()
     */
    public String getFileName() {
        return chromo.getFileName();
    }
    
    /**
     * Saves the current chromosome into a file
     * @throws IncorrectFileSizeException
     * @throws IOException
     */
    public void saveChromosome() throws IOException {
    	chromo.writeToFile();
    }

    /**
     * Resets the current chromosome into a new default chromosome
     * @throws IncorrectFileSizeException 
     * @throws IOException 
     */
    public void resetChromosome() throws IOException, IncorrectFileSizeException {
        chromo = new Chromosome();
    }
    
    /**
     * Loads the chromosome based on the new given file
     * @param f
     * @throws IOException
     * @throws IncorrectFileSizeException
     */
    public void loadNewChromosome(File f) throws IOException, IncorrectFileSizeException {
    	chromo.setFile(f);
    }
    
    /**
     * Mutates the chromosome based on the given rate and updates the assumed mutation rate
     * to the given rate
     * @param rate
     */
    public void mutateChromosome(double rate) {
    	chromo.mutate(rate);
    	mutationRate = rate;
    }
    
    /**
     * This is invoked when the text field is empty but the "mutate" button is still pressed.
     * Mutates the chromosome based on the default or last entered mutation rate.
     */
    public void mutateChromosome() {
    	mutateChromosome(mutationRate);
    }
}
