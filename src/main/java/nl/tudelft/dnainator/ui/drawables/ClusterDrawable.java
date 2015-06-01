package nl.tudelft.dnainator.ui.drawables;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import nl.tudelft.dnainator.core.SequenceNode;

/**
 * The {@link ClusterDrawable} class represents the mid level object in the viewable model.
 */
public class ClusterDrawable extends Group {
	private static final int CLUSTER_SIZE = 3;
	private List<SequenceNode> clustered;
	private Shape shape;

	/**
	 * Construct a new mid level {@link ClusterDrawable} using the default graph.
	 * @param clustered	the clustered {@link SequenceNode}s in this cluster.
	 */
	public ClusterDrawable(List<SequenceNode> clustered) {
		this.clustered = clustered;

		shape = new Circle(CLUSTER_SIZE, Color.BLUE);
		shape.setOnMouseClicked(e -> System.out.println(clustered));
		Text label = new Text(Integer.toString(clustered.size()));
		label.setStyle("-fx-font-size: 2pt");

		getChildren().add(shape);
		getChildren().add(label);
	}

	/**
	 * @return the {@link SequenceNode}s in this cluster.
	 */
	public List<SequenceNode> getClustered() {
		return clustered;
	}
}
