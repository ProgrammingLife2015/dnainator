package nl.tudelft.dnainator.graph.impl.query;

import nl.tudelft.dnainator.core.impl.Cluster;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * The {@link ClustersFromQuery} creates multiple {@link Cluster}s from all nodes,
 * starting at a list of startNodes, that have a base sequence shorter than the specified threshold.
 * It will only cluster nodes not yet marked as visited.
 */
public class ClustersFromQuery implements Query<Queue<Cluster>> {
	private Set<String> visited;
	private List<String> startNodes;
	private int threshold;

	/**
	 * Create a new {@link ClustersFromQuery}, which will:.
	 * - only cluster nodes that haven't been visited yet
	 * - use the specified threshold
	 * - return multiple clusters
	 * @param visited	the visited nodes
	 * @param startNodes	the list of start nodes
	 * @param threshold	the clustering threshold
	 */
	public ClustersFromQuery(Set<String> visited, List<String> startNodes, int threshold) {
		this.visited = visited;
		this.startNodes = startNodes;
		this.threshold = threshold;
	}

	@Override
	public Queue<Cluster> execute(GraphDatabaseService service) {
		Queue<Cluster> rootClusters = new LinkedList<Cluster>();

		for (String sn : startNodes) {
			// Continue if this startNode was consumed by another cluster
			if (visited.contains(sn)) {
				continue;
			}

			// Otherwise get the cluster starting from this startNode
			Cluster c = new ClusterQuery(visited, sn, threshold).execute(service);
			rootClusters.add(c);
		}

		return rootClusters;
	}

}
