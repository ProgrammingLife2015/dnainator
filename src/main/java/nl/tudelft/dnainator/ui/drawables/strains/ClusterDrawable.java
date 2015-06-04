package nl.tudelft.dnainator.ui.drawables.strains;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.MapChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.ColorServer;
import nl.tudelft.dnainator.ui.drawables.Drawable;

/**
 * The {@link ClusterDrawable} class represents the mid level object in the viewable model.
 */
public class ClusterDrawable extends Group implements Drawable {
	private static final int CLUSTER_SIZE = 3;
	private List<SequenceNode> clustered;
	private Set<String> sources;
	private Shape shape;
	private Text label;

	/**
	 * Construct a new mid level {@link ClusterDrawable} using the default graph.
	 * @param colorServer the {@link ColorServer} to bind to.
	 * @param clustered	the clustered {@link SequenceNode}s in this cluster.
	 */
	public ClusterDrawable(ColorServer colorServer, List<SequenceNode> clustered) {
		this.clustered = clustered;
		this.sources = new HashSet<>();
		for (SequenceNode n : clustered) {
			for (String src : n.getSource().split(",")) {
				sources.add(src);
			}
		}
		//this.sources = clustered.stream()
		//		.flatMap(node -> node.getSource().split(","))
		//		.collect(Collectors.toSet());
		shape = new Circle(CLUSTER_SIZE);
		shape.setOnMouseClicked(e -> System.out.println(clustered));
		label = new Text(Integer.toString(clustered.size()));
		label.setStyle("-fx-font-size: 2pt");

		checkForColor(colorServer);
		colorServer.addListener(change -> onColorServerChanged(change));

		getChildren().add(shape);
		getChildren().add(label);
	}

	private void checkForColor(ColorServer colorServer) {
		for (String source : sources) {
			String style = colorServer.getColor(source);
			if (style != null) {
				addStyle(style);
			}
		}
	}

	private void onColorServerChanged(
			MapChangeListener.Change<? extends String, ? extends String> change) {
		sources.stream().forEach(source -> {
			if (source.equals(change.getKey())) {
				if (change.wasAdded()) {
					addStyle(change.getValueAdded());
				} else {
					removeStyle(change.getValueRemoved());
				}
			}
		});
	}

	@Override
	public void addStyle(String style) {
		for (Node node : getChildren()) {
			node.getStyleClass().add(style);
		}
	}

	@Override
	public void removeStyle(String style) {
		for (Node node : getChildren()) {
			node.getStyleClass().remove(style);
		}
	}

	/**
	 * @return the {@link SequenceNode}s in this cluster.
	 */
	public List<SequenceNode> getClustered() {
		return clustered;
	}
}
