package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@link Strain} class represents the graph that contains the DNA strain.
 * It holds both content and children, and toggles what to load and display based on the zoom level.
 */
public class Strain extends Group {
	private static final int Y_OFFSET = 10;
	private static final int RANK_WIDTH = 10;
	private static final int NO_CLUSTERS = 33000;
	/* JavaFX scene graph cannot handle rectangles larger than 10k pixels, so we split a 30k
	* rectangle into 4 slices. */
	private static final int SLICES = 4;
	private static final int CLUSTER_DIVIDER = 100;

	private ColorServer colorServer;
	private Graph graph;
	private Map<String, ClusterDrawable> clusters;
	private Group content;
	private Group childContent;

	/**
	 * Construct a new top level {@link Strain} using the specified graph.
	 * @param colorServer The {@link ColorServer} to bind to.
	 * @param graph	The specified graph.
	 */
	public Strain(ColorServer colorServer, Graph graph) {
		this(colorServer, graph, new Group(), new Group());
	}

	/**
	 * Construct a new top level {@link Strain} using the specified graph, content and child
	 * content.
	 * @param colorServer The {@link ColorServer} to bind to.
	 * @param graph	The specified graph.
	 * @param content The specified graph content.
	 * @param childContent The specified child content.
	 */
	public Strain(ColorServer colorServer, Graph graph, Group content, Group childContent) {
		this.colorServer = colorServer;
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
	 * @param bounds	The given viewport bounds.
	 * @return	True if (partially) in viewport, false otherwise.
	 */
	public boolean isInViewport(Bounds bounds) {
		return bounds.contains(content.localToParent(0, 0));
	}

	/*
	 * Load the drawable content of the graph itself.
	 */
	private void loadContent() {
		for (int i = 0; i < SLICES; i++) {
			int width = NO_CLUSTERS * RANK_WIDTH / SLICES;
			Rectangle rectangle = new Rectangle(width, RANK_WIDTH, Color.BLACK);
			rectangle.setTranslateX(i * width);
			content.getChildren().add(rectangle);
		}
	}

	/*
	 * Load the drawable content of the graph's children, given the bounds of the viewing port.
	 */
	private void loadChildren(Bounds bounds) {
		int minRank = (int) (Math.max(bounds.getMinX() / RANK_WIDTH, 0));
		int maxRank = (int) (RANK_WIDTH + bounds.getMaxX() / RANK_WIDTH);

		System.out.println("load iteration: " + minRank + " -> " + maxRank);
		List<String> roots = graph.getRank(minRank).stream()
				.map(SequenceNode::getId).collect(Collectors.toList());
		Map<Integer, List<Cluster>> result = graph.getAllClusters(roots, maxRank,
				(int) (bounds.getWidth() / CLUSTER_DIVIDER));

		clusters.clear();
		childContent.getChildren().clear();

		result.forEach(this::loadRank);
		clusters.values().forEach(e -> loadEdges(bounds, e));
	}

	/*
	 * Load the drawable content of the edges for all displayed clusters.
	 */
	private void loadEdges(Bounds bounds, ClusterDrawable cluster) {
		childContent.getChildren().addAll(cluster.getCluster().getNodes().stream()
				.flatMap(e -> e.getOutgoing().stream())
				.filter(destid -> clusters.get(destid) != cluster)
				.map(destid -> {
					ClusterDrawable dest = clusters.get(destid);
					if (dest == null) {
						Edge e = new Edge(cluster, destid);
						e.getEdge().endYProperty().setValue(bounds.getMinY() + Y_OFFSET);
						return e;
					} else {
						return new Edge(cluster, dest);
					}
				})
				.collect(Collectors.toList()));
	}

	private void loadRank(Integer key, List<Cluster> value) {
		for (int i = 0; i < value.size(); i++) {
			ClusterDrawable cluster = new ClusterDrawable(colorServer, value.get(i));
			cluster.getCluster().getNodes().forEach(e -> clusters.put(e.getId(), cluster));
			cluster.setTranslateX(key * RANK_WIDTH);
			cluster.setTranslateY(i * RANK_WIDTH - value.size() * RANK_WIDTH / 2);
			childContent.getChildren().add(cluster);
		}
	}

	/**
	 * Toggle between displaying own content or children.
	 * @param bounds     The bounds of the viewport.
	 * @param visible    True if the content needs to be visible, false otherwise.
	 */
	public void toggle(Bounds bounds, boolean visible) {
		if (visible && !content.isVisible()) {
			childContent.getChildren().clear();
			content.setVisible(true);
			loadContent();
		}
		if (!visible) {
			content.setVisible(false);
			loadChildren(bounds);
		}
	}

	/**
	 * Update method that should be called after scaling.
	 * This method checks how zoomed in we are by transforming bounds to root coordinates,
	 * and then dynamically adds and deletes items in the JavaFX scene graph.
	 * @param bounds	The bounds of the viewport to update.
	 */
	public void update(Bounds bounds) {
		if (bounds.getWidth() > Thresholds.GRAPH.get()) {
			toggle(bounds, true);
		} else {
			toggle(bounds, false);
		}
	}
}
