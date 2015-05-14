package nl.tudelft.dnainator.ui.models;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

/**
 * The {@link GraphItem} class represents the top level object in the viewable model.
 * It is a {@link CompositeItem}, that can hold both content and children.
 */
public class GraphItem extends CompositeItem {
	private static final int NO_CLUSTERS = 10;

	/**
	 * Construct a new top level {@link GraphItem} using the default graph.
	 */
	public GraphItem() {
		super(Neo4jSingleton.getInstance().getDatabase());

		localToRootProperty().set(new Translate());

		setContent(new Rectangle(GRAPH_WIDTH, CLUSTER_SIZE, Color.BLACK));

		for (int i = 0; i < NO_CLUSTERS; i++) {
			ClusterItem ci = new ClusterItem(localToRootProperty());
			ci.setTranslateX(i * CLUSTER_WIDTH);
			getChildItems().add(ci);
		}
	}

	/**
	 * Update visibility for this node and children.
	 */
	@Override
	public void update(Bounds b) {
		if (b.getWidth() > Thresholds.GRAPH.get()) {
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
