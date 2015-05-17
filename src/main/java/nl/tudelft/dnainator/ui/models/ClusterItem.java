package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

/**
 * The {@link ClusterItem} class represents the mid level object in the viewable model.
 * Just like the {@link GraphItem} class, it's a {@link CompositeItem},
 * that can hold both content and children.
 */
public class ClusterItem extends CompositeItem {
	private static final String TYPE = "Cluster";

	/**
	 * Construct a new mid level {@link ClusterItem} using the default graph.
	 * Since a {@link ClusterItem} is not at the root of the model,
	 * it should bind its parent for correct positioning.
	 * @param parent	the concatenated transfrom from root to parent
	 */
	public ClusterItem(ObjectProperty<Transform> parent) {
		this(Neo4jSingleton.getInstance().getDatabase(), parent);
	}

	/**
	 * Construct a new mid level {@link ClusterItem} using the default graph.
	 * Since a {@link ClusterItem} is not at the root of the model,
	 * it should bind its parent for correct positioning.
	 * @param graph		the specified graph
	 * @param parent	the concatenated transfrom from root to parent
	 */
	public ClusterItem(Graph graph, ObjectProperty<Transform> parent) {
		super(graph);

		bindLocalToRoot(parent);

		getContent().getChildren().add(new Circle(CLUSTER_SIZE, Color.BLUE));

		// FIXME: These should be lazily instantiated!
		for (int i = 0; i < NO_RANKS; i++) {
			RankItem si = new RankItem(getGraph(), localToRootProperty());
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

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public List<String> getSources() {
		return null;
	}
}
