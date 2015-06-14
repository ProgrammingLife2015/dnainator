package nl.tudelft.dnainator.javafx.widgets;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.views.StrainView;

/**
 * The minimap gives a global overview of the DNA strains in the {@link StrainView}, based on the
 * amount of ranks.
 */
public class Minimap extends Pane {
	private static final double HEIGHT = 50;

	/**
	 * Instantiates a new {@link Minimap}.
	 * @param strain The {@link Strain} to operate on.
	 * @param graph The {@link Graph} that the {@link Strain} operates on.
	 */
	public Minimap(Strain strain, Graph graph) {
		setHeight(HEIGHT);

		Rectangle view = new Rectangle();
		view.setFill(Color.DARKRED);
		view.xProperty().bind(strain.minRankProperty());
		view.heightProperty().bind(heightProperty());
		view.widthProperty().bind(strain.maxRankProperty().subtract(strain.minRankProperty()));
		getChildren().add(view);
	}
}
