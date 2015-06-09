package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import nl.tudelft.dnainator.javafx.widgets.contexts.EdgeContext;

/**
 * The drawable edge is a line that can be bound to the a source and a destination cluster.
 */
public class Edge extends Group {
	private static final int Y_OFFSET = 80;
	private Line edge;

	/**
	 * Instantiate the source part of a new Edge.
	 * @param src This edge's source node.
	 */
	private Edge(ClusterDrawable src) {
		edge = new Line();
		edge.startXProperty().bind(src.translateXProperty());
		edge.startYProperty().bind(src.translateYProperty());
		edge.getStyleClass().add("drawable-edge");
		edge.setOnContextMenuRequested(e -> {
			EdgeContext.getInstance().show(Edge.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		getChildren().add(edge);
	}

	/**
	 * Instantiate a new Edge.
	 * @param src This edge's source node.
	 * @param dest This edge's destination node.
	 */
	public Edge(ClusterDrawable src, ClusterDrawable dest) {
		this(src);
		edge.endXProperty().bind(dest.translateXProperty());
		edge.endYProperty().bind(dest.translateYProperty());
	}

	/**
	 * Instantiate a new Edge.
	 * @param src This edge's source node.
	 * @param destid This edge's destination node (not in view).
	 */
	public Edge(ClusterDrawable src, String destid) {
		this(src);
		edge.endXProperty().bind(src.translateXProperty());
		edge.endYProperty().bind(src.translateYProperty().subtract(Y_OFFSET));

		Circle hidden = new Circle(2, Color.GREY);
		hidden.centerXProperty().bind(edge.endXProperty());
		hidden.centerYProperty().bind(edge.endYProperty());
		getChildren().add(hidden);
	}
}
