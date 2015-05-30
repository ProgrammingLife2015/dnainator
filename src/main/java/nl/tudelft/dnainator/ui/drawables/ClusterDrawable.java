package nl.tudelft.dnainator.ui.drawables;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import nl.tudelft.dnainator.core.SequenceNode;

/**
 * The {@link ClusterDrawable} class represents the mid level object in the viewable model.
 */
public class ClusterDrawable extends Group {
	private static final int CLUSTER_SIZE = 3;
	private List<SequenceNode> clustered;
	private Circle c;

	/**
	 * Construct a new mid level {@link ClusterDrawable} using the default graph.
	 * @param clustered	the clustered {@link SequenceNode}s in this cluster.
	 */
	public ClusterDrawable(List<SequenceNode> clustered) {
		this.clustered = clustered;

		c = new Circle(CLUSTER_SIZE, Color.BLUE);
		c.setOnMouseClicked(e -> System.out.println(clustered));
		Text t = new Text(Integer.toString(clustered.size()));
		t.setStyle("-fx-font-size: 2pt");

		getChildren().add(c);
		getChildren().add(t);
	}

	/**
	 * @return the {@link SequenceNode}s in this cluster
	 */
	public List<SequenceNode> getClustered() {
		return clustered;
	}
}
