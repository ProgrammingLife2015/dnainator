package nl.tudelft.dnainator.ui.models;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

/**
 * The {@link GraphItem} class represents the top level object in the viewable model.
 * It is a {@link CompositeItem}, that can hold both content and children.
 */
public class GraphItem extends CompositeItem {
	private static final int FOUR = 4;

	private Graph graph;
	private Map<String, NodeItem> nodes;

	/**
	 * Construct a new top level {@link GraphItem} using the default graph.
	 */
	public GraphItem() {
		this(Neo4jSingleton.getInstance().getDatabase());
	}

	/**
	 * Construct a new top level {@link GraphItem} using the specified graph.
	 * @param graph	the specified graph
	 */
	public GraphItem(Graph graph) {
		super(null, 0);
		this.graph = graph;
		this.nodes = new HashMap<>();

		for (int i = 0; i < FOUR; i++) {
			int width = NO_CLUSTERS * RANK_WIDTH * RANK_WIDTH / FOUR;
			Rectangle r = new Rectangle(width, RANK_WIDTH, Color.BLACK);
			r.setTranslateX(i * width);
			getContent().getChildren().add(r);
		}

		// FIXME: These should be lazily instantiated!
		load();
	}

	private void load() {
		for (int i = 0; i < NO_CLUSTERS; i++) {
			ClusterItem ci = new ClusterItem(this, i * RANK_WIDTH);
			getChildItems().add(ci);
		}
	}

	@Override
	public Graph getGraph() {
		return graph;
	}

	@Override
	public Map<String, NodeItem> getNodes() {
		return nodes;
	}

	@Override
	public ModelItem getRoot() {
		return this;
	}

	@Override
	public void update(Bounds b) {
		update(b, Thresholds.GRAPH);
	}
}
