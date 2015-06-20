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

import org.neo4j.collection.primitive.Primitive;
import org.neo4j.collection.primitive.PrimitiveLongSet;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.ArrayList;
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
	private List<Node> startNodes;
	private Set<Long> bubbleSourcesToKeepIntact;
	private PrimitiveLongSet visited;
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
		this.startNodes = new ArrayList<>(1);
		this.bubbleSourcesToKeepIntact = new HashSet<>();
		this.visited = Primitive.longSet();
	}

	private Iterable<Node> getNodesInRank(GraphDatabaseService service, int rank) {
		return IteratorUtil.loop(service.findNodes(NodeLabels.NODE,
				SequenceProperties.RANK.name(), rank));
	}

	private ResourceIterable<Node> withinRange(GraphDatabaseService service,
			int startRank, int endRank) {
		return withinRange(service, getNodesInRank(service, startRank), endRank,
				PathExpanders.forTypeAndDirection(RelTypes.NEXT, Direction.OUTGOING));
	}

	private ResourceIterable<Node> withinRange(GraphDatabaseService service,
			Iterable<Node> start, int endRank, PathExpander<Object> pe) {
		return service.traversalDescription()
				.breadthFirst()
				.expand(pe)
				.evaluator(new UntilRankEvaluator(endRank))
				.traverse(start).nodes();
	}

	@Override
	public Map<Integer, List<Cluster>> execute(GraphDatabaseService service) {
		// First determine which nodes are interesting.
		for (Node n : withinRange(service, minRank, maxRank)) {
			int interestingness = is.compute(new Neo4jScoreContainer(n));
			n.setProperty(SequenceProperties.INTERESTINGNESS.name(), interestingness);
			if (interestingness > threshold) {
				for (long sourceID : getBubbleIDs(n)) {
					bubbleSourcesToKeepIntact.add(sourceID);
				}
			}
		}
		// Then cluster everything, except for the bubbles in bubbleSourcesToKeepIntact.
		return cluster(service, minRank, maxRank);
	}

	private long[] getBubbleIDs(Node n) {
		return (long[]) n.getProperty(BubbleProperties.BUBBLE_SOURCE_IDS.name());
	}

	private Map<Integer, List<Cluster>> cluster(GraphDatabaseService service,
			int startRank, int endRank) {
		return cluster(service, getNodesInRank(service, startRank), endRank);
	}

	private Map<Integer, List<Cluster>> cluster(GraphDatabaseService service,
			Iterable<Node> startNodes, int endRank) {
		Map<Integer, List<Cluster>> result = new HashMap<Integer, List<Cluster>>();
		cluster(service, startNodes, endRank, result);
		return result;
	}

	private int recursionLevelGlobal = 0;
	private void cluster(GraphDatabaseService service,
			Iterable<Node> startNodes, int endRank, Map<Integer, List<Cluster>> acc) {
		int recursionLevel = recursionLevelGlobal;
		System.out.println("Begin Recursion level: " + recursionLevelGlobal++);
		for (Node n : withinRange(service, startNodes, endRank, BubbleSkipper.get())) {
			if (visited.contains(n.getId())) {
				return;
			}
			visited.add(n.getId());
			if (isSource(n)) {
				Node sink = getSinkFromSource(n);
				if (!isSink(n)) {
					putClusterInto(createSingletonCluster(service, n), acc);
				}
				putClusterInto(createSingletonCluster(service, sink), acc);
				if (bubbleSourcesToKeepIntact.contains(n.getId())) {
					System.out.println("Intact bubble: " + n.getProperty("ID"));
					int sinkRank = (int) sink.getProperty(SequenceProperties.RANK.name());
					this.startNodes.clear();
					n.getRelationships(RelTypes.NEXT, Direction.OUTGOING)
						.forEach(rel -> this.startNodes.add(rel.getEndNode()));
					cluster(service, this.startNodes, sinkRank - 1, acc);
				} else {
					System.out.println("Collapsed bubble: " + n.getProperty("ID"));
					// Cluster the bubble.
					putClusterInto(collapseBubble(service, n, sink), acc);
				}
			} else if (!n.hasRelationship(RelTypes.BUBBLE_SOURCE_OF)) {
				System.out.println("Singleton: " + n.getProperty("ID"));
				putClusterInto(createSingletonCluster(service, n), acc);
			}
		}
		System.out.println("End Recursion level: " + recursionLevel);
	}

	private boolean isSource(Node n) {
		return n.hasLabel(NodeLabels.BUBBLE_SOURCE);
	}

	private boolean isSink(Node n) {
		return n.hasRelationship(RelTypes.BUBBLE_SOURCE_OF, Direction.INCOMING);
	}

	private static Node getSinkFromSource(Node source) {
		return source.getSingleRelationship(RelTypes.BUBBLE_SOURCE_OF, Direction.OUTGOING)
				.getEndNode();
	}

	private Cluster createSingletonCluster(GraphDatabaseService service, Node n) {
		EnrichedSequenceNode sn = new Neo4jSequenceNode(service, n);
		return new Cluster((int) n.getProperty(SequenceProperties.RANK.name()),
				Collections.singletonList(sn), sn.getAnnotations());
	}

	private Cluster collapseBubble(GraphDatabaseService service,
			Node source, Node sink) {
		int sourceRank = (int) source.getProperty(SequenceProperties.RANK.name());
		int sinkRank = (int) sink.getProperty(SequenceProperties.RANK.name());
		// Set the rank of the cluster to be in the middle.
		int clusterRank = sourceRank + (sinkRank - sourceRank) / 2;

		List<EnrichedSequenceNode> nodes =
				nodesWithinBubble(service, sourceRank, sinkRank, source, sink)
				.map(n -> new Neo4jSequenceNode(service, n))
				.collect(Collectors.toList());
		List<Annotation> annotations = nodes.stream()
				.flatMap(e -> e.getAnnotations().stream())
				.collect(Collectors.toList());
		Cluster cluster = new Cluster(clusterRank, nodes, annotations);
		return cluster;
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
		if (sinkRank - sourceRank > 30) {
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

	private void putClusterInto(Cluster c, Map<Integer, List<Cluster>> into) {
		if (into.containsKey(c.getStartRank())) {
			into.get(c.getStartRank()).add(c);
		} else {
			List<Cluster> cs = new ArrayList<>();
			cs.add(c);
			into.put(c.getStartRank(), cs);
		}
	}

	private static <T> Stream<T> stream(Iterable<T> in) {
		// Quick utility method, for converting iterables to streams.
	    return StreamSupport.stream(in.spliterator(), false);
	}
}
