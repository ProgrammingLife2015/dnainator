package nl.tudelft.dnainator.ui.drawables.strains;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.MapChangeListener;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.ColorServer;
import nl.tudelft.dnainator.ui.drawables.Drawable;

/**
 * The {@link ClusterDrawable} class represents the mid level object in the viewable model.
 */
public class ClusterDrawable extends Group implements Drawable {
	private static final int CLUSTER_SIZE = 3;
	private static final int PIETHRESHOLD = 10;
	private List<SequenceNode> clustered;
	private Set<String> sources;
	private Text label;
	private Pie pie;

	/**
	 * Construct a new mid level {@link ClusterDrawable} using the default graph.
	 * @param colorServer the {@link ColorServer} to bind to.
	 * @param clustered	the clustered {@link SequenceNode}s in this cluster.
	 */
	public ClusterDrawable(ColorServer colorServer, List<SequenceNode> clustered) {
		this.clustered = clustered;
		this.sources = clustered.stream()
				.flatMap(e -> Arrays.asList(e.getSource().split(",")).stream())
				.collect(Collectors.toSet());

		label = new Text(Integer.toString(clustered.size()));
		label.setStyle("-fx-font-size: 2pt");

		if (sources.size() > PIETHRESHOLD) {
			getChildren().add(new Circle(CLUSTER_SIZE));
		} else {
			colorServer.addListener(this::onColorServerChanged);

			List<String> collect = sources.stream()
					.map(e -> colorServer.getColor(e))
					.filter(e -> e != null)
					.collect(Collectors.toList());
			pie = new Pie(CLUSTER_SIZE, collect);
			getChildren().add(pie);
		}
		getChildren().add(label);
	}

	private void onColorServerChanged(
			MapChangeListener.Change<? extends String, ? extends String> change) {
		if (!sources.contains(change.getKey())) {
			return;
		} else if (change.wasAdded()) {
			addStyle(change.getValueAdded());
		} else if (change.wasRemoved()) {
			removeStyle(change.getValueRemoved());
		}
	}

	@Override
	public void addStyle(String style) {
		pie.getStyles().add(style);
	}

	@Override
	public void removeStyle(String style) {
		pie.getStyles().remove(style);
	}

	/**
	 * @return the {@link SequenceNode}s in this cluster.
	 */
	public List<SequenceNode> getClustered() {
		return clustered;
	}
}
