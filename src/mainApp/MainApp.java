package mainApp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**this should be the good version
 * Class: MainApp
 * @author S23_R301
 * <br>Purpose: Top level class for CSSE220 Project containing main method 
 * <br>Restrictions: None
 * 
 */
public class MainApp {
	private final String FRAME_TITLE = "Chromosome Thingiemabob";
	private final String GENERATION_FRAME_TITLE = "Evolution Viewer";
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 700;
    private final int DELAY = 50;
    public static final int GENERATION_FRAME_WIDTH = 1200;
    public static final int GENERATION_FRAME_HEIGHT = 600;
    
	
	/**
	 * Manages the general structure of the app. Serves as the graphics viewer class that manages the 
	 * JFrame and component and includes the buttons and their listeners. All possible exceptions are
	 * all so tried and caught in this class.
	 * 
	 * @throws IOException
	 * @throws IncorrectFileSizeException
	 */
	private void runApp() throws IOException, IncorrectFileSizeException {
		Generation g = new Generation(100010100, 100, 100);
		//Set up JFrame and ChromosomeComponent
		JFrame frame = new JFrame();
		frame.setTitle(FRAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChromosomeComponent chromosome = new ChromosomeComponent();
		frame.add(chromosome, BorderLayout.CENTER);
		frame.setTitle(chromosome.getFileName());
		frame.setVisible(true);
		
		JFrame generationFrame = new JFrame();
		generationFrame.setTitle(GENERATION_FRAME_TITLE);
		generationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GenerationComponent generation = new GenerationComponent();
		generationFrame.add(generation, BorderLayout.CENTER);
		generationFrame.setVisible(true);
		
		//Generation Frame buttons & misc
		JPanel labelPanel = new JPanel();
		frame.add(labelPanel, BorderLayout.NORTH);
		
		JLabel generationViewerLabel = new JLabel("Generation Viewer");
		labelPanel.add(generationViewerLabel);
		
		JPanel interactionPanel = new JPanel();
		generationFrame.add(interactionPanel, BorderLayout.SOUTH);
		
		JLabel mutationTextLabel = new JLabel("Mutation Rate (decimal) ");
		JTextField mutationTextField = new JTextField(3);
		interactionPanel.add(mutationTextLabel);
		interactionPanel.add(mutationTextField);
		
		String[] selectionMethod = {"Truncation", "Roulette", "Rank"};
		JLabel selectionTextLabel = new JLabel("Selection ");
		JComboBox selectionBox = new JComboBox(selectionMethod);
		interactionPanel.add(selectionTextLabel);
		interactionPanel.add(selectionBox);
		
		JLabel crossoverYN = new JLabel("Crossover Y/N?");
		JCheckBox crossoverCheck = new JCheckBox();
		interactionPanel.add(crossoverYN);
		interactionPanel.add(crossoverCheck);
		
		JLabel populationSizeLabel = new JLabel("Population Size ");
		JTextField populationTextField = new JTextField(3);
		interactionPanel.add(populationSizeLabel);
		interactionPanel.add(populationTextField);
		
		JLabel generationLabel = new JLabel("Generations ");
		JTextField generationTextField = new JTextField(5);
		interactionPanel.add(generationLabel);
		interactionPanel.add(generationTextField);
		
		JLabel genomeLengthLabel = new JLabel("Genome Length ");
		JTextField genomeTextField = new JTextField(3);
		interactionPanel.add(genomeLengthLabel);
		interactionPanel.add(genomeTextField);
		
		JLabel elitismLabel = new JLabel("Elitism (decimal) ");
		JTextField elitismTextField = new JTextField(3);
		interactionPanel.add(elitismLabel);
		interactionPanel.add(elitismTextField);
		
		JButton evolutionButton = new JButton("Start Evolution");
		interactionPanel.add(evolutionButton);
		
		generationFrame.pack();
		generationFrame.setSize(GENERATION_FRAME_WIDTH, GENERATION_FRAME_HEIGHT);
		
		
		//Manage buttons and text field
		JPanel mutatePanel = new JPanel();
		frame.add(mutatePanel, BorderLayout.SOUTH);
		
		JButton mutateButton = new JButton("Mutate");
		mutatePanel.add(mutateButton);
		
		JTextField textField = new JTextField(3);
		JLabel textFieldLabel = new JLabel("Mutation Rate (decimal): ");
		mutatePanel.add(textFieldLabel);
		mutatePanel.add(textField);

		class MutateListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					chromosome.mutateChromosome(Double.parseDouble(textField.getText())); 
				} catch(NumberFormatException e1){ //make sure the code doesn't crash if the text field is empty
					chromosome.mutateChromosome();
				}
				System.out.println("Mutate chromosome.");
				chromosome.repaint();
			}
		}
		
		mutateButton.addActionListener(new MutateListener());
		
		JButton loadButton = new JButton("Load");
		mutatePanel.add(loadButton);
		
		class LoadListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Load chromosome.");
				try {
					JFileChooser jf = new JFileChooser();
					jf.showOpenDialog(null);
					File f = jf.getSelectedFile();
					chromosome.loadNewChromosome(f);
					chromosome.repaint();
					frame.setTitle(chromosome.getFileName());
				} catch (IncorrectFileSizeException e2) {
					e2.printErrorMessage();
				} catch (Exception e1) {
					System.err.println("Unknown Exception. Please try again.");
				} 
			}
			
		}
		
		loadButton.addActionListener(new LoadListener());
		
		JButton saveButton = new JButton("Save");
		mutatePanel.add(saveButton);
		
		class SaveListener implements ActionListener {
			
			// Something's wrong here because yous shouldn't print the exceptions
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					chromosome.saveChromosome();
				} catch (IOException e1) {
					System.out.println("IO Exception.");
				} 
				
			}
			
		}
		
		saveButton.addActionListener(new SaveListener());
		
		frame.pack();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		Timer t = new Timer(DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				generation.update();
				generation.repaint();
			}
		});
		
		//Starts the simulator
		t.start();
	} // runApp

	/**
	 * ensures: runs the application
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		MainApp mainApp = new MainApp();
		try {
			mainApp.runApp();
		} catch (IncorrectFileSizeException e) {

			e.printStackTrace();
		}		
	} // main

}