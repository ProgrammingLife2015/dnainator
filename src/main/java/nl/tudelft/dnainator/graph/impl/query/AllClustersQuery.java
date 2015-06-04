package nl.tudelft.dnainator.graph.impl.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import nl.tudelft.dnainator.core.impl.Cluster;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The {@link AllClustersQuery} creates {@link Cluster}s from all nodes,
 * starting at the startNodes, and ending when the maximum specified start rank is reached.
 */
public class AllClustersQuery implements Query<Map<Integer, List<Cluster>>> {
	private List<String> startNodes;
	private int threshold;
	private int maxRank;

	/**
	 * Create a new {@link AllClustersQuery}, which will:.
	 * - start clustering at the specified startNodes
	 * - stop clustering when the end rank is reached
	 * - use the specified clustering threshold
	 * @param startNodes	the start nodes
	 * @param maxRank	the maximum rank
	 * @param threshold	the clustering threshold
	 */
	public AllClustersQuery(List<String> startNodes, int maxRank, int threshold) {
		this.startNodes = startNodes;
		this.maxRank = maxRank;
		this.threshold = threshold;
	}

	@Override
	public Map<Integer, List<Cluster>> execute(GraphDatabaseService service) {
		Queue<Cluster> rootClusters = new PriorityQueue<>((e1, e2) -> 
			e1.getStartRank() - e2.getStartRank()
		);
		Set<String> visited = new HashSet<>();
		Map<Integer, List<Cluster>> result = new HashMap<Integer, List<Cluster>>();
	
		// Retrieve the root clusters starting from the startNodes and add them to the queue
		ClustersFromQuery clusterFrom = new ClustersFromQuery(visited, startNodes, threshold);
		rootClusters.addAll(clusterFrom.execute(service));

		// Find adjacent clusters as long as there are root clusters in the queue
		int minrank = rootClusters.stream().mapToInt(e -> e.getStartRank()).min().orElse(0);
		while (!rootClusters.isEmpty()) {
			Cluster c = rootClusters.poll();
			if (c.getStartRank() < minrank || c.getStartRank() > maxRank) {
				continue;
			}
			result.putIfAbsent(c.getStartRank(), new ArrayList<>());
			result.get(c.getStartRank()).add(c);

			c.getNodes().forEach(sn -> {
				ClustersFromQuery cfq = new ClustersFromQuery(visited,
										sn.getOutgoing(), threshold);
				rootClusters.addAll(cfq.execute(service));
			});
		}

		return result;
	}
}
