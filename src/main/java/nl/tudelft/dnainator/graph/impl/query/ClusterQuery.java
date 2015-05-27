package nl.tudelft.dnainator.graph.impl.query;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ID;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.NODELABEL;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.RANK;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;
import nl.tudelft.dnainator.graph.impl.RelTypes;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.TraversalDescription;

public class ClusterQuery implements Query<Cluster> {
	private Set<String> visited;
	private String start;
	private int threshold;
	
	public ClusterQuery(Set<String> visited, String start, int threshold) {
		this.visited = visited;
		this.start = start;
		this.threshold = threshold;
	}
	
	@Override
	public Cluster execute(GraphDatabaseService service) {
		Cluster cluster = null;
		
		try (Transaction tx = service.beginTx()) {
			Label nodeLabel = DynamicLabel.label(NODELABEL.name());
			Node startNode = service.findNode(nodeLabel, ID.name(), start);
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
			List<SequenceNode> retrieve = result.stream().map(e -> Neo4jGraph.createSequenceNode(e))
							.collect(Collectors.toList());
			cluster = new Cluster(rankStart, retrieve);
			
			tx.success();
		}
	
		return cluster;
	}
}

