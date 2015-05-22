package nl.tudelft.dnainator.ui.drawables;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A label for use in a phylogenetic tree.
 */
public class PhylogeneticLabel extends Text {

	/**
	 * Constructs a new {@link PhylogeneticLabel}.
	 * @param x The horizontal position of this label.
	 * @param y The vertical position of this label.
	 * @param text The text to be contained in this label.
	 */
	public PhylogeneticLabel(double x, double y, String text) {
		super(x, y, text);
		setTextAlignment(TextAlignment.CENTER);
	}
}
