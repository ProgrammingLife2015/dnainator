package nl.tudelft.dnainator.ui.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private void load(int rankStart, int rankEnd) {
		if (rankStart < 0) {
			rankStart = 0;
		}
		if (rankEnd > NO_RANKS) {
			rankEnd = NO_RANKS;
		}

		while (rankStart <= rankEnd) {
			List<SequenceNode> roots = getGraph().getRank(rankStart);
			Map<Integer, List<Cluster>> clusters = getGraph().getClusters(roots, 2000);
			int maxRank = rankStart;
			for (Entry<Integer, List<Cluster>> rankedClusters : clusters.entrySet()) {
				int clusterRank = rankedClusters.getKey();
				if (clusterRank > maxRank) {
					maxRank = clusterRank;
				}
				RankItem r = new RankItem(this, clusterRank, rankedClusters.getValue());
				getChildItems().add(r);
			}
			rankStart = maxRank;
		}
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
		if (!isInViewport(b)) {
			return;
		}
		int start = (int) b.getMinX() / RANK_WIDTH;
		int end = (int) b.getMaxX() / RANK_WIDTH;
		System.out.println(start + " to " + end);
		load(start, end);
		update(b, Thresholds.GRAPH);
	}
}
