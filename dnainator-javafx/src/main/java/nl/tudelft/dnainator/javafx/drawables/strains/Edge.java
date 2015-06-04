package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import java.util.List;
import nl.tudelft.dnainator.javafx.views.AbstractView;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;

/**
 * The drawable edge is a line that can be bound to the a source and a destination cluster.
 */
public class Edge extends Group implements Propertyable {
	private Line edge;
	private static final String TYPE = "Edge";
	private ClusterDrawable src;
	private ClusterDrawable dst;

	/**
	 * Instantiate the source part of a new Edge.
	 * @param src This edge's source node.
	 */
	private Edge(ClusterDrawable src) {
		this.src = src;
		setOnMouseClicked(e -> AbstractView.setLastClicked(this));
		edge = new Line();
		edge.startXProperty().bind(src.translateXProperty());
		edge.startYProperty().bind(src.translateYProperty());
		getChildren().add(edge);
	}

	/**
	 * Instantiate a new Edge.
	 * @param src This edge's source node.
	 * @param dest This edge's destination node.
	 */
	public Edge(ClusterDrawable src, ClusterDrawable dest) {
		this(src);
		this.dst = src;
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

		VBox box = new VBox(new Text(destid), new Circle(2));
		box.setAlignment(Pos.CENTER);
		box.translateXProperty().bind(edge.endXProperty().subtract(box.widthProperty().divide(2)));
		box.translateYProperty().bind(edge.endYProperty().subtract(box.heightProperty()));
		getChildren().add(box);
	}

	/**
	 * Return the line this Edge contains.
	 * @return	the line
	 */
	public Line getEdge() {
		return edge;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	/**
	 * All the sources that both src and destination contained.
	 */
	@Override
	public List<String> getSources() {
		if (dst != null) {
			src.getSources().retainAll(dst.getSources());
		}
		return src.getSources();
	}
}
