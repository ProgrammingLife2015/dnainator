package nl.tudelft.dnainator.ui.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

/**
 * The {@link GraphItem} class represents the top level object in the viewable model.
 * It is a {@link CompositeItem}, that can hold both content and children.
 */
public class GraphItem extends CompositeItem {
	private static final int FOUR = 4;

	private Graph graph;
	private Map<String, ClusterItem> clusters;

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
		this.clusters = new HashMap<>();

		for (int i = 0; i < FOUR; i++) {
			int width = NO_CLUSTERS * RANK_WIDTH * RANK_WIDTH / FOUR;
			Rectangle r = new Rectangle(width, RANK_WIDTH, Color.BLACK);
			r.setTranslateX(i * width);
			getContent().getChildren().add(r);
		}

	}

	private void load() {
		System.out.println("load iteration");

		List<SequenceNode> roots = getGraph().getRank(0);
		Map<Integer, List<Cluster>> clusters = getGraph().getClusters(roots, 1000);

		clusters.forEach((k, v) -> getChildItems().add(new RankItem(this, k, v)));
	}

	@Override
	public Graph getGraph() {
		return graph;
	}

	@Override
	public Map<String, ClusterItem> getClusters() {
		return clusters;
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
