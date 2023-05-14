package mainApp;

import javax.swing.JComponent;

public class PopulationComponent extends JComponent{
	private SimpleChromosome[] generation;
	
	public PopulationComponent(SimpleChromosome[] g) {
		this.generation = g;
	}
	
}
