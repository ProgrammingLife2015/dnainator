package nl.tudelft.dnainator.ui.drawables;

import javafx.scene.shape.Line;
import nl.tudelft.dnainator.ui.widgets.contexts.EdgeContext;

/**
 * The drawable edge is a line that can be bound to the a source and a destination cluster.
 */
public class DrawableEdge extends Line {
	/**
	 * Instantiate a new DrawableEdge.
	 * @param src This edge's source node.
	 * @param dest This edge's destination node.
	 */
	public DrawableEdge(ClusterDrawable src, ClusterDrawable dest) {
		getStyleClass().add("drawable-edge");
		setOnContextMenuRequested(e -> {
			EdgeContext.getInstance().show(DrawableEdge.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		
		startXProperty().bind(src.translateXProperty());
		startYProperty().bind(src.translateYProperty());
		endXProperty().bind(dest.translateXProperty());
		endYProperty().bind(dest.translateYProperty());
	}
}
