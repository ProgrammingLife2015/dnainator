package nl.tudelft.dnainator.ui.models;

import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.drawables.DrawableEdge;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The {@link ClusterItem} class represents the mid level object in the viewable model.
 * Just like the {@link GraphItem} class, it's a {@link CompositeItem},
 * that can hold both content and children.
 */
public class ClusterItem extends CompositeItem {
	private static final int CLUSTER_SIZE = 20;
	private List<SequenceNode> clustered;
	private Group edges;

	/**
	 * Construct a new mid level {@link ClusterItem} using the default graph.
	 * Every {@link ClusterItem} needs a reference to its parent.
	 * @param parent	the parent of this {@link ClusterItem}
	 * @param rank		the rank of this {@link ClusterItem}
	 * @param clustered	the clustered {@link SequenceNode}s in this cluster.
	 */
	public ClusterItem(ModelItem parent, int rank, List<SequenceNode> clustered) {
		super(parent, rank);
		this.clustered = clustered;
		this.edges = new Group();
		// Point for each node the cluster it is in.
		clustered.forEach(node -> getClusters().put(node.getId(), this));
		getContent().setTranslateX(rank * RANK_WIDTH);
		getContent().getChildren().add(edges);
		getContent().getChildren().add(new Circle(CLUSTER_SIZE, Color.BLUE));
	}

	private void load() {
		if (getChildItems().size() != 0) {
			return;
		}
		for (SequenceNode sn : clustered) {
			for (String out : sn.getOutgoing()) {
				ClusterItem cluster = getClusters().get(out);
				if (cluster == this) {
					continue;
				}
				if (cluster != null) {
					edges.getChildren().add(new DrawableEdge(this, cluster));
				}
			}
		}
	}

	@Override
	public void update(Bounds b) {
		if (!isInViewport(b)) {
			return;
		}

		load();
		update(b, Thresholds.CLUSTER);
	}

	/**
	 * @return the {@link SequenceNode}s in this cluster
	 */
	public List<SequenceNode> getClustered() {
		return clustered;
	}

}
