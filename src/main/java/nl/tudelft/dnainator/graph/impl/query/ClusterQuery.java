package nl.tudelft.dnainator.graph.impl.query;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ID;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.RANK;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.impl.Neo4jSequenceNode;
import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.TraversalDescription;

/**
 * The {@link ClusterQuery} creates one single {@link Cluster} from all nodes,
 * starting at the start, that have a base sequence shorter than the specified threshold.
 * It will only cluster nodes not yet marked as visited.
 */
public class ClusterQuery implements Query<Cluster> {
	private Set<String> visited;
	private String start;
	private int threshold;

	/**
	 * Create a new {@link ClusterQuery}, which will:.
	 * - only cluster nodes that haven't been visited yet
	 * - use the specified threshold
	 * - only return a single cluster
	 * @param visited	the visited nodes
	 * @param start		the start node
	 * @param threshold	the clustering threshold
	 */
	public ClusterQuery(Set<String> visited, String start, int threshold) {
		this.visited = visited;
		this.start = start;
		this.threshold = threshold;
	}

	@Override
	public Cluster execute(GraphDatabaseService service) {
		Cluster cluster = null;
		Node startNode = service.findNode(NodeLabels.NODE, ID.name(), start);
		List<Node> result = new ArrayList<>();

		// A depth first traversal traveling along both incoming and outgoing edges.
		TraversalDescription clusterDesc = service.traversalDescription()
						.depthFirst()
						.relationships(RelTypes.NEXT, Direction.BOTH)
						.evaluator(new ClusterEvaluator(threshold, visited));
		// Traverse the cluster starting from the startNode.
		int rankStart = (int) startNode.getProperty(RANK.name());
		for (Node end : clusterDesc.traverse(startNode).nodes()) {
			result.add(end);

			// Update this cluster's start rank according to the lowest node rank.
			int endRank = (int) startNode.getProperty(RANK.name());
			if (endRank < rankStart) {
				rankStart = endRank;
			}
		}

		// Might want to internally pass nodes.
		List<SequenceNode> retrieve = result.stream().map(e -> new Neo4jSequenceNode(service, e))
						.collect(Collectors.toList());
		cluster = new Cluster(rankStart, retrieve);

		return cluster;
	}
}

