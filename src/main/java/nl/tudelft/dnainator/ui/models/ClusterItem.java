package nl.tudelft.dnainator.ui.models;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The {@link ClusterItem} class represents the mid level object in the viewable model.
 * Just like the {@link GraphItem} class, it's a {@link CompositeItem},
 * that can hold both content and children.
 */
public class ClusterItem extends CompositeItem {
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

	@Override
	public void update(Bounds b) {
		if (!isInViewport(b)) {
			return;
		}

		update(b, Thresholds.CLUSTER);
	}

}
