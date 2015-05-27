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

public class AllClustersQuery implements Query<Map<Integer, List<Cluster>>> {
	private List<String> startNodes;
	private int threshold;
	private int end;

	public AllClustersQuery(List<String> startNodes, int end, int threshold) {
		this.startNodes = startNodes;
		this.end = end;
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
		while (!rootClusters.isEmpty()) {
			Cluster c = rootClusters.poll();
			if (c.getStartRank() > end) {
				break;
			}
			// TODO: Might want to introduce a QueryResult class instead of this map
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
