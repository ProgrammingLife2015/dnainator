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
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	private Map<String, Object> nodesInBubbleParameters;
	private static final String GET_NODES_IN_BUBBLE = "match (n: " + NodeLabels.NODE.name() + ") "
			+ "where n." + SequenceProperties.RANK.name() + " > {sourceRank} "
			+ "and n." + SequenceProperties.RANK.name() + " < {sinkRank} "
			+ "and {sourceID} in n." + BubbleProperties.BUBBLE_SOURCE_IDS.name() + " return n";

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
		this.nodesInBubbleParameters = new HashMap<>(2 + 1);
	}

	private ResourceIterable<Node> untilMaxRank(GraphDatabaseService service) {
		Iterable<Node> start = IteratorUtil.loop(service.findNodes(NodeLabels.NODE,
				SequenceProperties.RANK.name(), minRank));
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
				.relationships(RelTypes.NEXT, Direction.OUTGOING)
				.traverse(start).nodes();
	}

	@Override
	public Map<Integer, List<Cluster>> execute(GraphDatabaseService service) {
		Map<Integer, List<Cluster>> individualNodes = new HashMap<>();
		Set<Long> bubbleSourcesNested = new HashSet<>();
		Set<Long> bubbleSourcesToCluster = new HashSet<>();
		Set<Long> bubbleSourcesToKeepIntact = new HashSet<>();
		for (Node n : untilMaxRank(service)) {
			if (n.hasLabel(NodeLabels.BUBBLE_SOURCE)) {
				bubbleSourcesToCluster.add(n.getId());
				if (getBubbleIDs(n).length > 0) {
					bubbleSourcesNested.add(n.getId());
				}
			} else {
				if (getBubbleIDs(n).length == 0) {
					Cluster individualNode = createSingletonCluster(service, n);
					individualNodes.put(individualNode.getStartRank(),
							Collections.singletonList(individualNode));
				}
			}
			int interestingness = is.compute(new Neo4jScoreContainer(n));
			n.setProperty(SequenceProperties.INTERESTINGNESS.name(), interestingness);
			if (interestingness > threshold) {
				for (long sourceID
						: (long[]) n.getProperty(BubbleProperties.BUBBLE_SOURCE_IDS.name())) {
					bubbleSourcesToKeepIntact.add(sourceID);
					bubbleSourcesToCluster.remove(sourceID);
				}
			}
		}
		bubbleSourcesToCluster.removeAll(bubbleSourcesNested);
		bubbleSourcesToKeepIntact.removeAll(bubbleSourcesNested);
		return mergeMaps(Stream.of(individualNodes,
				cluster(service, bubbleSourcesToCluster, bubbleSourcesToKeepIntact)));
	}

	private long[] getBubbleIDs(Node n) {
		return (long[]) n.getProperty(BubbleProperties.BUBBLE_SOURCE_IDS.name());
	}

	private Map<Integer, List<Cluster>> cluster(GraphDatabaseService service,
			Set<Long> bubbleSourcesToCluster, Set<Long> bubbleSourcesToKeepIntact) {
		Stream<Map<Integer, List<Cluster>>> bubblesClustered = bubbleSourcesToCluster.stream()
				.map(service::getNodeById)
				.map(source -> collapseBubble(service, source, getSinkFromSource(source)));
		Stream<Map<Integer, List<Cluster>>> singletonClusters = bubbleSourcesToKeepIntact.stream()
				.map(service::getNodeById)
				.map(source -> getSingletonClusters(service, source, getSinkFromSource(source)));
		return mergeMaps(Stream.concat(bubblesClustered, singletonClusters));
	}

	private static Node getSinkFromSource(Node source) {
		return source.getSingleRelationship(RelTypes.BUBBLE_SOURCE_OF, Direction.OUTGOING)
				.getEndNode();
	}

	private Map<Integer, List<Cluster>> getSingletonClusters(GraphDatabaseService service,
			Node source, Node sink) {
		int sourceRank = (int) source.getProperty(SequenceProperties.RANK.name());
		int sinkRank = (int) sink.getProperty(SequenceProperties.RANK.name());
		Map<Integer, List<Cluster>> single =
				nodesWithinBubble(service, sourceRank, sinkRank, source, sink)
				.map(n -> createSingletonCluster(service, n))
				.collect(Collectors.groupingBy(Cluster::getStartRank));
		single.put(sourceRank, Collections.singletonList(createSingletonCluster(service, source)));
		single.put(sinkRank, Collections.singletonList(createSingletonCluster(service, sink)));
		return single;
	}

	private Cluster createSingletonCluster(GraphDatabaseService service, Node n) {
		EnrichedSequenceNode sn = new Neo4jSequenceNode(service, n);
		return new Cluster((int) n.getProperty(SequenceProperties.RANK.name()),
				Collections.singletonList(sn), sn.getAnnotations());
	}

	private Map<Integer, List<Cluster>> collapseBubble(GraphDatabaseService service,
			Node source, Node sink) {
		Map<Integer, List<Cluster>> res = new HashMap<>(2 + 1); // source + sink + bubble.
		int sourceRank = (int) source.getProperty(SequenceProperties.RANK.name());
		int sinkRank = (int) sink.getProperty(SequenceProperties.RANK.name());
		// Set the rank of the cluster to be in the middle.
		int clusterRank = sourceRank + (sinkRank - sourceRank) / 2;
		res.put(sourceRank, Collections.singletonList(createSingletonCluster(service, source)));
		res.put(sinkRank, Collections.singletonList(createSingletonCluster(service, sink)));

		List<EnrichedSequenceNode> nodes =
				nodesWithinBubble(service, sourceRank, sinkRank, source, sink)
				.map(n -> new Neo4jSequenceNode(service, n))
				.collect(Collectors.toList());
		List<Annotation> annotations = nodes.stream()
				.flatMap(e -> e.getAnnotations().stream())
				.collect(Collectors.toList());
		Cluster cluster = new Cluster(clusterRank, nodes, annotations);
		res.put(clusterRank, Collections.singletonList(cluster));
		return res;
	}

	private Iterable<Node> trimPath(Path path) {
		if (path.length() < 2) {
			return Collections.emptyList();
		}
		Iterator<Node> nodes = path.nodes().iterator();
		List<Node> res = new ArrayList<>(path.length() - 1);
		nodes.next();
		for (int i = 1; i <= path.length() - 1; i++) {
			res.add(nodes.next());
		}
		return res;
	}

	private Stream<Node> nodesWithinBubble(GraphDatabaseService service,
			int sourceRank, int sinkRank, Node source, Node sink) {
		if (sinkRank - sourceRank > 20) {
			nodesInBubbleParameters.put("sourceRank", sourceRank);
			nodesInBubbleParameters.put("sinkRank", sinkRank);
			nodesInBubbleParameters.put("sourceID", source.getId());
			return stream(IteratorUtil.loop(
				service.execute(GET_NODES_IN_BUBBLE, nodesInBubbleParameters).columnAs("n")
			));
		} else {
			return stream(GraphAlgoFactory.allSimplePaths(
				PathExpanders.forTypeAndDirection(RelTypes.NEXT, Direction.OUTGOING),
				sinkRank - sourceRank).findAllPaths(source, sink))
					.flatMap(path -> stream(trimPath(path)))
					.distinct();
		}
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
