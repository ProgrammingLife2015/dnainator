package nl.tudelft.dnainator.graph.impl.query;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.impl.Neo4jScoreContainer;
import nl.tudelft.dnainator.graph.impl.Neo4jSequenceNode;
import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.impl.properties.BubbleProperties;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;
import nl.tudelft.dnainator.graph.interestingness.InterestingnessStrategy;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The {@link AllClustersQuery} creates {@link Cluster}s from all nodes,
 * between the given ranks using the given threshold value.
 */
public class AllClustersQuery implements Query<Map<Integer, List<Cluster>>> {
	private int minRank;
	private int maxRank;
	private int threshold;
	private InterestingnessStrategy is;

	/**
	 * Create a new {@link AllClustersQuery}, which will:.
	 * - get all clusters between the given ranks
	 * - use the specified clustering threshold
	 * @param minRank	the minimum rank
	 * @param maxRank	the maximum rank
	 * @param threshold	the clustering threshold
	 * @param is the interestingness strategy, which determines how the
	 * interestingness score is calculated.
	 */
	public AllClustersQuery(int minRank, int maxRank, int threshold,
			InterestingnessStrategy is) {
		this.minRank = minRank;
		this.maxRank = maxRank;
		this.threshold = threshold;
		this.is = is;
	}

	private TraversalDescription untilMaxRank(GraphDatabaseService service) {
		return service.traversalDescription()
				.breadthFirst()
				.evaluator(path -> {
					if ((int) path.endNode().getProperty(SequenceProperties.RANK.name())
							<= maxRank) {
						return Evaluation.INCLUDE_AND_CONTINUE;
					} else {
						return Evaluation.EXCLUDE_AND_PRUNE;
					}
				})
				.relationships(RelTypes.NEXT, Direction.OUTGOING);
	}

	@Override
	public Map<Integer, List<Cluster>> execute(GraphDatabaseService service) {
		Set<Long> bubbleSourcesToCluster = new HashSet<>();
		Set<Long> bubbleSourcesToKeepIntact = new HashSet<>();
		Iterable<Node> start = IteratorUtil.loop(service.findNodes(NodeLabels.NODE,
				SequenceProperties.RANK.name(), minRank));
		for (Node n : untilMaxRank(service).traverse(start).nodes()) {
			if (n.hasLabel(NodeLabels.BUBBLE_SOURCE)) {
				bubbleSourcesToCluster.add(n.getId());
			}
			int interestingness = is.compute(new Neo4jScoreContainer(n));
			if (interestingness > threshold) {
				for (long sourceID
						: (long[]) n.getProperty(BubbleProperties.BUBBLE_SOURCE_IDS.name())) {
					bubbleSourcesToKeepIntact.add(sourceID);
					bubbleSourcesToCluster.remove(sourceID);
				}
			}
		}
		return cluster(service, bubbleSourcesToCluster, bubbleSourcesToKeepIntact);
	}

	private Map<Integer, List<Cluster>> cluster(GraphDatabaseService service,
			Set<Long> bubbleSourcesToCluster, Set<Long> bubbleSourcesToKeepIntact) {
		Map<Integer, List<Cluster>> bubblesClustered = bubbleSourcesToCluster.stream()
				.map(service::getNodeById)
				.map(source -> collapseBubble(service, source, getSinkFromSource(source)))
				.collect(Collectors.groupingBy(Cluster::getStartRank));
		Stream<Map<Integer, List<Cluster>>> singletonClusters = bubbleSourcesToKeepIntact.stream()
				.map(service::getNodeById)
				.map(source -> getSingletonClusters(service, source, getSinkFromSource(source)));
		return mergeMaps(Stream.concat(Stream.of(bubblesClustered), singletonClusters));
	}

	private static Node getSinkFromSource(Node source) {
		return source.getSingleRelationship(RelTypes.BUBBLE_SOURCE_OF, Direction.OUTGOING)
				.getEndNode();
	}

	private Map<Integer, List<Cluster>> getSingletonClusters(GraphDatabaseService service,
			Node source, Node sink) {
		int sourceRank = (int) source.getProperty(SequenceProperties.RANK.name());
		int sinkRank = (int) sink.getProperty(SequenceProperties.RANK.name());
		PathFinder<Path> withinBubble = pathFinderBetweenRanks(sourceRank, sinkRank);
		return stream(withinBubble.findAllPaths(source, sink))
				.flatMap(path -> stream(path.nodes()))
				.distinct()
				.map(n -> createSingletonCluster(service, n))
				.collect(Collectors.groupingBy(Cluster::getStartRank));
	}

	private Cluster createSingletonCluster(GraphDatabaseService service, Node n) {
		EnrichedSequenceNode sn = new Neo4jSequenceNode(service, n);
		return new Cluster((int) n.getProperty(SequenceProperties.RANK.name()),
				Collections.singletonList(sn), sn.getAnnotations());
	}

	private Cluster collapseBubble(GraphDatabaseService service, Node source, Node sink) {
		int sourceRank = (int) source.getProperty(SequenceProperties.RANK.name());
		int sinkRank = (int) sink.getProperty(SequenceProperties.RANK.name());
		int clusterRank = sourceRank + (sinkRank - sourceRank) / 2;
		PathFinder<Path> withinBubble = pathFinderBetweenRanks(sourceRank, sinkRank);
		// FIXME: don't collapse source and sink, keep those intact.
		List<EnrichedSequenceNode> nodes = stream(
				withinBubble.findAllPaths(source, sink))
				.flatMap(path -> stream(path.nodes()))
				.distinct()
				.map(n -> new Neo4jSequenceNode(service, n))
				.collect(Collectors.toList());
		List<Annotation> annotations = nodes.stream()
				.flatMap(e -> e.getAnnotations().stream())
				.collect(Collectors.toList());
		return new Cluster(clusterRank, nodes, annotations);
	}

	private PathFinder<Path> pathFinderBetweenRanks(int minRank, int maxRank) {
		return GraphAlgoFactory.allSimplePaths(
				PathExpanders.forTypeAndDirection(RelTypes.NEXT, Direction.OUTGOING),
				maxRank - minRank);
	}

	private Map<Integer, List<Cluster>> mergeMaps(Stream<Map<Integer, List<Cluster>>> concat) {
		return concat.map(Map::entrySet)
				.flatMap(Collection::stream)
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (left, right) -> {
					List<Cluster> newList = new ArrayList<>(left.size() + right.size());
					newList.addAll(right);
					newList.addAll(left);
					return left;
				}));
	}

	private static <T> Stream<T> stream(Iterable<T> in) {
		// Quick utility method, for converting iterables to streams.
	    return StreamSupport.stream(in.spliterator(), false);
	}
}
