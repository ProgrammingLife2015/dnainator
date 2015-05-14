package nl.tudelft.dnainator.ui.models;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

/**
 * The {@link ClusterItem} class represents the mid level object in the viewable model.
 * Just like the {@link GraphItem} class, it's a {@link CompositeItem},
 * that can hold both content and children.
 */
public class ClusterItem extends CompositeItem {
	private static final int NO_RANKS = 10;

	/**
	 * Construct a new mid level {@link ClusterItem} using the default graph.
	 * Since a {@link ClusterItem} is not at the root of the model,
	 * it should bind its parent for correct positioning.
	 * @param parent	the concatenated transfrom from root to parent
	 */
	public ClusterItem(ObjectProperty<Transform> parent) {
		super(Neo4jSingleton.getInstance().getDatabase());

		bindLocalToRoot(parent);

		setContent(new Circle(CLUSTER_SIZE, Color.BLUE));

		for (int i = 0; i < NO_RANKS; i++) {
			RankItem si = new RankItem(localToRootProperty());
			si.setTranslateX(i * RANK_WIDTH);
			getChildItems().add(si);
		}
	}

	@Override
	public void update(Bounds b) {
		if (!isInViewport(b)) {
			return;
		}

		if (b.getWidth() > Thresholds.CLUSTER.get()) {
			if (!getContent().isVisible()) {
				getContent().setVisible(true);
				getChildRoot().getChildren().clear();
			}
		} else {
			if (getContent().isVisible()) {
				getContent().setVisible(false);
				getChildRoot().getChildren().addAll(getChildItems());
			}

			for (ModelItem m : getChildItems()) {
				m.update(b);
			}
		}
	}
}
