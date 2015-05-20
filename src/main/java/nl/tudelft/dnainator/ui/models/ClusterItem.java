package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import nl.tudelft.dnainator.core.SequenceNode;

/**
 * The {@link ClusterItem} class represents the mid level object in the viewable model.
 * Just like the {@link GraphItem} class, it's a {@link CompositeItem},
 * that can hold both content and children.
 */
public class ClusterItem extends CompositeItem {
	private static final String TYPE = "Cluster";

	/**
	 * Construct a new mid level {@link ClusterItem} using the default graph.
	 * Every {@link ClusterItem} needs a reference to its parent.
	 * @param parent	the parent of this {@link ClusterItem}
	 */
	public ClusterItem(ModelItem parent) {
		super(parent);

		bindLocalToRoot(parent.localToRootProperty());

		getContent().getChildren().add(new Circle(CLUSTER_SIZE, Color.BLUE));

		// FIXME: These should be lazily instantiated!
		for (int i = 0; i < NO_RANKS; i++) {
			RankItem si = new RankItem(this);
			si.setTranslateX(i * RANK_WIDTH);
			getChildItems().add(si);
		}
	}

	private void load() {
		if (getContent().getChildren().size() == 1) {
			int rank = (int) localToRoot(new Point2D(0, 0)).getX() / RANK_WIDTH;

			List<SequenceNode> start = getGraph().getRank(rank);
			List<SequenceNode> nodes = getGraph().getCluster(start.get(0).getId(), 2000);
			for (int i = 0; i < nodes.size(); i++) {
				NodeItem drawable = new NodeItem(this, nodes.get(i));
				drawable.setTranslateY(i * RANK_WIDTH - nodes.size() * RANK_WIDTH / 2);

				getContent().getChildren().add(drawable);
			}
			System.out.println("loaded " + rank);
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

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public List<String> getSources() {
		return null;
	}
}
