package nl.tudelft.dnainator.ui.drawables.phylogeny;

import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

/**
 * An edge for use in a phylogenetic tree.
 */
public class Edge extends Path {
	/**
	 * Constructs a new {@link Edge}, ending at the provided {@link AbstractNode}.
	 * @param dst This edge's destination node.
	 */
	public Edge(AbstractNode dst) {
		// Set start point of the Path.
		MoveTo m = new MoveTo();
		m.xProperty().bindBidirectional(dst.translateXProperty());
		m.yProperty().bindBidirectional(dst.translateYProperty());
		getElements().add(m);

		// Add the horizontal line, starting at the path's start point.
		HLineTo hline = new HLineTo();
		getElements().add(hline);

		// Add the vertical line, starting at the horizontal line's end point.
		VLineTo vline = new VLineTo();
		getElements().add(vline);

		getStyleClass().add("phylogenetic-edge");
	}
}
