package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.Range;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.SemanticDrawable;
import nl.tudelft.dnainator.javafx.drawables.annotations.Connection;
import nl.tudelft.dnainator.javafx.drawables.annotations.Gene;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * The {@link Strain} class represents the graph that contains the DNA strain.
 */
public class Strain extends SemanticDrawable {
	// Max zoom is 15. 15*53.33 = 800, which is our max threshold.
	private static final double THRESHOLD_FACTOR = 53.33;
	private static final double ANNOTATION_HEIGHT = 50;
	private static final double OFFSET = 30;
	private ColorServer colorServer;
	private Graph graph;
	private double zoomInBound;
	private LinkedHashMap<String, ClusterDrawable> clusters;
	private Range lastLoaded;
	private Thresholds lastThreshold;

	/**
	 * Construct a new top level {@link Strain} using the specified graph.
	 *
	 * @param colorServer The {@link ColorServer} to bind to.
	 * @param graph The specified graph.
	 * @param zoomInBound The bound on zooming in.
	 */
	public Strain(ColorServer colorServer, Graph graph, double zoomInBound) {
		this(colorServer, graph, zoomInBound, new Group());
	}

	/**
	 * Construct a new top level {@link Strain} using the specified graph, content and child
	 * content.
	 *
	 * @param colorServer The {@link ColorServer} to bind to.
	 * @param graph	The specified graph.
	 * @param content The specified graph content.
	 * @param zoomInBound The bound on zooming in.
	 */
	public Strain(ColorServer colorServer, Graph graph, double zoomInBound, Group content) {
		super(content);
		this.colorServer = colorServer;
		this.graph = graph;
		this.zoomInBound = zoomInBound;
		this.clusters = new LinkedHashMap<>();
		this.lastLoaded = new Range(Integer.MAX_VALUE, Integer.MIN_VALUE);
		this.lastThreshold = Thresholds.INDIVIDUAL;
	}

	@Override
	public void update(Bounds bounds, double zoom) {
		int minRank = (int) (Math.max(bounds.getMinX() / RANK_WIDTH, 0));
		int maxRank = (int) (RANK_WIDTH + bounds.getMaxX() / RANK_WIDTH);
		minRank = Math.min(Math.max(minRank, 0), graph.getMaxRank());
		maxRank = Math.min(Math.max(minRank, maxRank), graph.getMaxRank());
		minRankProperty().set(minRank);
		maxRankProperty().set(maxRank);
		loadContent(new Range(minRank, maxRank), zoom);
	}

	@Override
	protected void loadContent(Range ranks, double zoom) {
		double interestingness = (zoomInBound - zoom) * THRESHOLD_FACTOR;
		Thresholds newThreshold = Thresholds.retrieve(interestingness);

		if (!needRedraw(ranks, newThreshold)) {
			return;
		}
		System.out.println("load iteration: " + ranks.getX() + " -> " + ranks.getY()
				+ " with zoom level " + zoom + " (" + interestingness + ": " + lastThreshold + ")");

		List<Annotation> annotations = getSortedAnnotations(ranks);
		List<String> roots = graph.getRank(lastLoaded.getX()).stream()
				.map(SequenceNode::getId)
				.sorted((s1, s2) -> s1.compareTo(s2))
				.collect(Collectors.toList());
		Map<Integer, List<Cluster>> result = graph.getAllClusters(roots, lastLoaded.getY(),
				lastThreshold.get());

		content.getChildren().clear();
		clusters.clear();
		result.forEach(this::loadRank);
		clusters.values().forEach(this::loadEdges);
		drawAnnotations(annotations);
		content.getChildren().addAll(clusters.values().stream().distinct()
				.collect(Collectors.toList()));
	}

	private boolean needRedraw(Range ranks, Thresholds newThreshold) {
		if (ranks.getX() < lastLoaded.getX() || ranks.getY() > lastLoaded.getY()
				|| lastThreshold.compareTo(newThreshold) != 0) {
			int offset = (ranks.getY() - ranks.getX()) / 2;
			lastLoaded = new Range(Math.max(ranks.getX() - offset, 0), ranks.getY() + offset);
			lastThreshold = newThreshold;
			return true;
		}
		return false;
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
			content.getChildren().add(new Connection(g.translateXProperty().add(0),
					g.translateYProperty().add(0), left.translateXProperty().add(0),
					left.translateYProperty().add(0)));
		}
		if (right != null) {
			content.getChildren().add(new Connection(g.translateXProperty().add(
					g.widthProperty()), g.translateYProperty().add(0),
					right.translateXProperty().add(0), right.translateYProperty().add(0)));
		}
		if (left != null && right != null) {
			g.translateXProperty().bind(Bindings.add(left.translateXProperty(), Bindings.subtract(
					Bindings.divide(right.translateXProperty().subtract(left.translateXProperty()),
							2), g.widthProperty().divide(2))));
			if (prev != null && g.getLayoutBounds().intersects(prev.getLayoutBounds())) {
				g.translateYProperty().bind(prev.translateYProperty()
						.add(prev.heightProperty()).add(ANNOTATION_HEIGHT));
			} else {
				g.translateYProperty().bind(left.translateYProperty().add(ANNOTATION_HEIGHT));
			}
		}
		content.getChildren().add(g);
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

	private void loadRank(Integer key, List<Cluster> value) {
		for (int i = 0; i < value.size(); i++) {
			ClusterDrawable cluster = new ClusterDrawable(colorServer, value.get(i));
			cluster.getCluster().getNodes().forEach(e -> clusters.put(e.getId(), cluster));
			cluster.setTranslateX(key * RANK_WIDTH);
			cluster.setTranslateY(i * RANK_WIDTH - value.size() * RANK_WIDTH / 2);
		}
	}

	private void loadEdges(ClusterDrawable cluster) {
		content.getChildren().addAll(cluster.getCluster().getNodes().stream()
				.flatMap(e -> e.getOutgoing().stream())
				.filter(destid -> clusters.get(destid) != cluster)
				.map(destid -> {
					ClusterDrawable dest = clusters.get(destid);
					if (dest == null) {
						Edge e = new Edge(cluster, destid);
						e.getStyleClass().add("ghost");
						e.getEdge().endYProperty().setValue(-OFFSET);
						return e;
					} else {
						return new Edge(cluster, dest);
					}
				})
				.collect(Collectors.toList()));
	}

	/**
	 * @return the clusters in the view.
	 */
	public Map<String, ClusterDrawable> getClusters() {
		return clusters;
	}
}

