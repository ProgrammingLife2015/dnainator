package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.Range;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.annotations.Connection;
import nl.tudelft.dnainator.javafx.drawables.SemanticDrawable;
import nl.tudelft.dnainator.javafx.drawables.annotations.Gene;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * The {@link Strain} class represents the graph that contains the DNA strain.
 * It holds both content and children, and toggles what to load and display based on the zoom level.
 */
public class Strain extends SemanticDrawable {
	private static final int OFFSET = 10;
	private static final double ANNOTATION_HEIGHT = 50;
	private static final int RANK_WIDTH = 10;
	private static final int CLUSTER_DIVIDER = 100;

	private ColorServer colorServer;
	private Graph graph;
	private Map<String, ClusterDrawable> clusters;
	private IntegerProperty minRank = new SimpleIntegerProperty(0, "minRank");
	private IntegerProperty maxRank = new SimpleIntegerProperty(0, "maxRank");

	/**
	 * Construct a new top level {@link Strain} using the specified graph.
	 *
	 * @param colorServer The {@link ColorServer} to bind to.
	 * @param graph The specified graph.
	 */
	public Strain(ColorServer colorServer, Graph graph) {
		this(colorServer, graph, new Group(), new Group());
	}

	/**
	 * Construct a new top level {@link Strain} using the specified graph, content and child
	 * content.
	 *
	 * @param colorServer  The {@link ColorServer} to bind to.
	 * @param graph        The specified graph.
	 * @param content      The specified graph content.
	 * @param childContent The specified child content.
	 */
	public Strain(ColorServer colorServer, Graph graph, Group content, Group childContent) {
		super(content, childContent);
		this.colorServer = colorServer;
		this.graph = graph;
		this.clusters = new HashMap<>();
	}

	@Override
	protected void loadContent(Bounds bounds) {
		content.getChildren().clear();
		HBox hbox = new HBox();
		Range ranks = getRange(bounds);

		List<Annotation> annotations = getSortedAnnotations(ranks);
		annotations.forEach(a -> hbox.getChildren().add(new Gene(a)));
		content.getChildren().add(hbox);
	}

	@Override
	public void loadChildren(Bounds bounds) {
		Range ranks = getRange(bounds);

		System.out.println("load iteration: " + ranks.getX() + " -> " + ranks.getY());
		List<String> roots = graph.getRank(ranks.getX()).stream()
				.map(SequenceNode::getId).collect(Collectors.toList());
		List<Annotation> annotations = getSortedAnnotations(ranks);
		Map<Integer, List<Cluster>> result = graph.getAllClusters(roots, ranks.getY(),
				(int) (bounds.getWidth() / CLUSTER_DIVIDER));
		clusters.clear();
		childContent.getChildren().clear();

		result.forEach(this::loadRank);
		clusters.values().forEach(e -> loadEdges(bounds, e));
		drawAnnotations(annotations);
	}

	private Range getRange(Bounds bounds) {
		int min = (int) (Math.max(bounds.getMinX() / RANK_WIDTH, 0));
		int max = (int) (RANK_WIDTH + bounds.getMaxX() / RANK_WIDTH);
		min = Math.max(min, 0);
		min = Math.min(min, graph.getMaxRank());
		max = Math.max(min, max);
		max = Math.min(max, graph.getMaxRank());
		minRank.set(min);
		maxRank.set(max);
		return new Range(min, max);
	}

	private List<Annotation> getSortedAnnotations(Range ranks) {
		return graph.getAnnotationByRank(ranks).stream()
				.sorted((a1, a2) -> Integer.compare(a1.getStart(), a2.getStart()))
				.collect(Collectors.toList());
	}

	private void drawAnnotations(Collection<Annotation> annotations) {
		Gene prev = null;
		for (Annotation a : annotations) {
			prev = loadAnnotations(a, prev);
		}
	}

	private Gene loadAnnotations(Annotation annotation, Gene prev) {
		Gene g = new Gene(annotation);
		ClusterDrawable left = getClusterDrawable(annotation, Double.MAX_VALUE,
				(x, acc) -> x <= acc);
		ClusterDrawable right = getClusterDrawable(annotation, Double.MIN_VALUE,
				(x, acc) -> x >= acc);
		if (left != null) {
			childContent.getChildren().add(new Connection(g.translateXProperty().add(0),
					g.translateYProperty().add(0), left.translateXProperty().add(0),
					left.translateYProperty().add(0)));
		}
		if (right != null) {
			childContent.getChildren().add(new Connection(g.translateXProperty().add(
					g.widthProperty()), g.translateYProperty().add(0),
					right.translateXProperty().add(0), right.translateYProperty().add(0)));
		}
		if (left != null && right != null) {
			g.translateXProperty().bind(Bindings.add(left.translateXProperty(), Bindings.subtract(
					Bindings.divide(right.translateXProperty().subtract(left.translateXProperty()),
							2), g.widthProperty().divide(2))));
			if (prev != null && g.getLayoutBounds().intersects(prev.getLayoutBounds())) {
				g.translateYProperty().bind(prev.translateYProperty().add(ANNOTATION_HEIGHT));
			} else {
				g.translateYProperty().bind(left.translateYProperty().add(ANNOTATION_HEIGHT));
			}
		}
		childContent.getChildren().add(g);
		return g;
	}

	private ClusterDrawable getClusterDrawable(Annotation annotation, double startValue,
	                                           BiFunction<Double, Double, Boolean> function) {
		Collection<String> ids = annotation.getAnnotatedNodes();
		ClusterDrawable res = null;
		double acc = startValue;

		for (String id : ids) {
			ClusterDrawable cluster = clusters.get(id);
			if (cluster == null) {
				continue;
			}
			if (function.apply(cluster.getTranslateX(), acc)) {
				res = cluster;
				acc = cluster.getTranslateX();
			}
		}
		return res;
	}

	private void loadEdges(Bounds bounds, ClusterDrawable cluster) {
		childContent.getChildren().addAll(cluster.getCluster().getNodes().stream()
				.flatMap(e -> e.getOutgoing().stream())
				.filter(destid -> clusters.get(destid) != cluster)
				.map(destid -> {
					ClusterDrawable dest = clusters.get(destid);
					if (dest == null) {
						Edge e = new Edge(cluster, destid);
						e.getEdge().endYProperty().setValue(bounds.getMinY() + OFFSET);
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
	 * @return The minimum rank property.
	 */
	public IntegerProperty minRankProperty() {
		return minRank;
	}

	/**
	 * @return The maximum rank property.
	 */
	public IntegerProperty maxRankProperty() {
		return maxRank;
	}

	/**
	 * @return the clusters in the view.
	 */
	public Map<String, ClusterDrawable> getClusters() {
		return clusters;
	}

	/**
	 * Get the rank width.
	 * @return the rank width.
	 */
	public int getRankWidth() {
		return RANK_WIDTH;
	}
}
