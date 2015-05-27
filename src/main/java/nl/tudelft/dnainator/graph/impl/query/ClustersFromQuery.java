package nl.tudelft.dnainator.graph.impl.query;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import nl.tudelft.dnainator.core.impl.Cluster;

import org.neo4j.graphdb.GraphDatabaseService;

public class ClustersFromQuery implements Query<Queue<Cluster>> {
	private Set<String> visited;
	private List<String> startNodes;
	private int threshold;

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
