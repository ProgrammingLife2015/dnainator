package nl.tudelft.dnainator.ui.drawables;

import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

/**
 * An edge for use in a phylogenetic tree.
 */
public class PhylogeneticEdge extends Path {
	private HLineTo hline;
	private VLineTo vline;

	/**
	 * Constructs a new {@link PhylogeneticEdge}, starting at the provided
	 * {@link PhylogeneticNode}, extending to <code>levelWidth</code>.
	 * @param dst This edge's destination node.
	 * @param levelWidth This edge's width.
	 */
	public PhylogeneticEdge(PhylogeneticNode dst, double levelWidth) {
		// Set start point of the Path.
		MoveTo m = new MoveTo();
		m.xProperty().bindBidirectional(dst.centerXProperty());
		m.yProperty().bindBidirectional(dst.centerYProperty());
		getElements().add(m);

		// Add the horizontal line, starting at the path's start point.
		hline = new HLineTo(levelWidth);
		getElements().add(hline);

		// Add the vertical line, starting at the path's horizontal line's end point.
		vline = new VLineTo();
		getElements().add(vline);

		getStyleClass().add("phylogenetic-edge");
	}

	/**
	 * Binds this {@link PhylogeneticEdge}'s vertical line to <code>src</code>.
	 * @param src The {@link PhylogeneticNode} to bind this {@link VLineTo}'s end value to.
	 */
	public void bindTo(PhylogeneticNode src) {
		vline.yProperty().bindBidirectional(src.centerYProperty());
	}
}
