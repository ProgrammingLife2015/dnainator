package nl.tudelft.dnainator.ui.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.drawables.ClusterDrawable;
import nl.tudelft.dnainator.ui.drawables.DrawableEdge;

/**
 * The {@link GraphItem} class represents the graph that contains the DNA strain.
 * It holds both content and children, and toggles what to load and display based on the zoom level.
 */
public class GraphItem extends Group {
	private static final int RANK_WIDTH = 10;
	private static final int NO_CLUSTERS = 33000;
	private static final int FOUR = 4;
	private static final int CLUSTER_DIVIDER = 100;

	private Graph graph;
	private Map<String, ClusterDrawable> clusters;
	private Group content;
	private Group childContent;

	/**
	 * Construct a new top level {@link GraphItem} using the default graph.
	 */
	public GraphItem() {
		this(Neo4jSingleton.getInstance().getDatabase(), new Group(), new Group());
	}

	/**
	 * Construct a new top level {@link GraphItem} using the specified graph.
	 * @param graph	the specified graph
	 */
	public GraphItem(Graph graph) {
		this(graph, new Group(), new Group());
	}

	/**
	 * Construct a new top level {@link GraphItem} using the specified graph.
	 * @param graph	the specified graph
	 * @param content the specified graph content
	 * @param childContent the specified child content
	 */
	public GraphItem(Graph graph, Group content, Group childContent) {
		this.graph = graph;
		this.clusters = new HashMap<>();
		this.content = content;
		this.childContent = childContent;

		getChildren().add(content);
		getChildren().add(childContent);
		loadContent();
	}

	/**
	 * Check whether this object intersects with the given viewport bounds.
	 * @param b	the given viewport bounds
	 * @return	true when (partially) in viewport, false otherwise
	 */
	public boolean isInViewport(Bounds b) {
		return b.contains(content.localToParent(0, 0));
	}

	/*
	 * Load the drawable content of the graph itself.
	 */
	private void loadContent() {
		for (int i = 0; i < FOUR; i++) {
			int width = NO_CLUSTERS * RANK_WIDTH / FOUR;
			Rectangle r = new Rectangle(width, RANK_WIDTH, Color.BLACK);
			r.setTranslateX(i * width);
			content.getChildren().add(r);
		}
	}

	/*
	 * Load the drawable content of the graph's children, given the bounds of the viewing port.
	 */
	private void loadChildren(Bounds b) {
		int minrank = (int) (Math.max(b.getMinX() / RANK_WIDTH, 0));
		int maxrank = (int) (b.getMaxX() / RANK_WIDTH);

		System.out.println("load iteration: " + minrank + " -> " + maxrank);
		List<String> roots = graph.getRank(minrank).stream()
				.map(e -> e.getId()).collect(Collectors.toList());
		Map<Integer, List<Cluster>> result = graph.getAllClusters(roots, maxrank,
				(int) (b.getWidth() / CLUSTER_DIVIDER));

		clusters.clear();
		childContent.getChildren().clear();

		result.forEach(this::loadRank);
		clusters.values().forEach(this::loadEdges);
	}

	/*
	 * Load the drawable content of the edges for all displayed clusters.
	 */
	private void loadEdges(ClusterDrawable d) {
		childContent.getChildren().addAll(
				d.getClustered().stream().flatMap(e -> e.getOutgoing().stream())
				.filter(i -> clusters.containsKey(i))
				.filter(i -> clusters.get(i) != d)
				.map(o -> new DrawableEdge(d, clusters.get(o)))
				.collect(Collectors.toList()));
	}

	private void loadRank(Integer k, List<Cluster> v) {
		for (int i = 0; i < v.size(); i++) {
			ClusterDrawable c = new ClusterDrawable(v.get(i).getNodes());
			c.getClustered().forEach(e -> clusters.put(e.getId(), c));
			c.setTranslateX(k * RANK_WIDTH);
			c.setTranslateY(i * RANK_WIDTH - v.size() * RANK_WIDTH / 2);
			childContent.getChildren().add(c);
		}
	}

	/**
	 * Toggle between displaying own content or children.
	 * @param b		the bounds of the viewport
	 * @param visible	true for visible
	 */
	public void toggle(Bounds b, boolean visible) {
		if (visible && !content.isVisible()) {
			childContent.getChildren().clear();
			content.setVisible(true);
			loadContent();
		}
		if (!visible) {
			content.setVisible(false);
			loadChildren(b);
		}
	}

	/**
	 * Update method that should be called after scaling.
	 * This method checks how zoomed in we are by transforming bounds to root coordinates,
	 * and then dynamically adds and deletes items in the JavaFX scene graph.
	 * @param b	the bounds of the viewport to update
	 */
	public void update(Bounds b) {
		if (b.getWidth() > Thresholds.GRAPH.get()) {
			toggle(b, true);
		} else {
			toggle(b, false);
		}
	}
}
