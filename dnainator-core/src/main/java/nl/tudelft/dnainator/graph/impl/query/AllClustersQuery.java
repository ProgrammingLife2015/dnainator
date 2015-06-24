package nl.tudelft.dnainator.graph.impl.query;

import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.impl.Neo4jSequenceNode;
import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;
import nl.tudelft.dnainator.graph.interestingness.InterestingnessStrategy;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.TraversalDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link AllClustersQuery} creates {@link Cluster}s from all nodes,
 * starting at the startNodes, and ending when the maximum specified start rank is reached.
 */
public class AllClustersQuery implements Query<Map<Integer, List<Cluster>>> {
	private Set<String> visited;
	private List<String> startNodes;
	private int threshold;
	private int maxRank;
	private InterestingnessStrategy is;

	/**
	 * Create a new {@link AllClustersQuery}, which will:.
	 * - start clustering at the specified startNodes
	 * - stop clustering when the end rank is reached
	 * - use the specified clustering threshold
	 * @param startNodes	the start nodes
	 * @param maxRank	the maximum rank
	 * @param threshold	the clustering threshold
	 * @param is the interestingness strategy, which determines how the
	 * interestingness score is calculated.
	 */
	public AllClustersQuery(List<String> startNodes, int maxRank, int threshold,
			InterestingnessStrategy is) {
		this.startNodes = startNodes;
		this.maxRank = maxRank;
		this.threshold = threshold;
		this.visited = new HashSet<>();
		this.is = is;
	}

	@Override
	public Map<Integer, List<Cluster>> execute(GraphDatabaseService service) {
		Queue<Cluster> rootClusters = new PriorityQueue<>((e1, e2) -> 
			e1.getStartRank() - e2.getStartRank()
		);
		Map<Integer, List<Cluster>> result = new HashMap<>();
	
		rootClusters.addAll(clustersFrom(service, startNodes));

		// Find adjacent clusters as long as there are root clusters in the queue
		int minrank = rootClusters.stream().mapToInt(Cluster::getStartRank).min().orElse(0);
		while (!rootClusters.isEmpty()) {
			Cluster c = rootClusters.poll();
			if (c.getStartRank() < minrank || c.getStartRank() > maxRank) {
				continue;
			}
			result.putIfAbsent(c.getStartRank(), new ArrayList<>());
			result.get(c.getStartRank()).add(c);

			c.getNodes().forEach(sn ->
					rootClusters.addAll(clustersFrom(service, sn.getOutgoing())));
		}

		return result;
	}

	private Queue<Cluster> clustersFrom(GraphDatabaseService service, List<String> startNodes) {
		Queue<Cluster> rootClusters = new LinkedList<>();

		for (String sn : startNodes) {
			// Continue if this startNode was consumed by another cluster
			if (visited.contains(sn)) {
				continue;
			}

			// Otherwise get the cluster starting from this startNode
			rootClusters.add(cluster(service, sn));
		}

		return rootClusters;
	}

	private Cluster cluster(GraphDatabaseService service, String start) {
		Cluster cluster;
		Node startNode = service.findNode(NodeLabels.NODE, SequenceProperties.ID.name(), start);
		List<Node> result = new ArrayList<>();

		// A depth first traversal traveling along both incoming and outgoing edges.
		TraversalDescription clusterDesc = service.traversalDescription()
						.depthFirst()
						.relationships(RelTypes.NEXT, Direction.BOTH)
						.evaluator(new ClusterEvaluator(threshold, visited, is));
		// Traverse the cluster starting from the startNode.
		int rankStart = (int) startNode.getProperty(SequenceProperties.RANK.name());
		for (Node end : clusterDesc.traverse(startNode).nodes()) {
			result.add(end);

			// Update this cluster's start rank according to the lowest node rank.
			int endRank = (int) startNode.getProperty(SequenceProperties.RANK.name());
			if (endRank < rankStart) {
				rankStart = endRank;
			}
		}
		// Might want to internally pass nodes.
		List<EnrichedSequenceNode> retrieve = result.stream()
				.map(e -> new Neo4jSequenceNode(service, e))
				.collect(Collectors.toList());
		cluster = new Cluster(rankStart, retrieve);
		return cluster;
	}
}
