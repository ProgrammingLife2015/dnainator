package nl.tudelft.dnainator.graph.impl.query;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ID;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.SEQUENCE;

import java.util.Set;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

/**
 * Evaluates whether a node is part of a cluster based on the given threshold.
 */
public class ClusterEvaluator implements Evaluator {
	private int threshold;
	private Set<String> visited;

	/**
	 * Create a new {@link ClusterEvaluator}, which will:.
	 * - only cluster nodes that haven't been visited yet
	 * - use the specified threshold
	 * @param threshold	the clustering threshold
	 * @param visited	the visited nodes
	 */
	public ClusterEvaluator(int threshold, Set<String> visited) {
		this.threshold = threshold;
		this.visited = visited;
	}

	/**
	 * Evaluates a node and determines whether to include and / or continue.
	 * Continues on and returns exactly those nodes that:
	 * - haven't been visited yet and
	 *   - are the start node
	 *   - have a sequence < threshold (and thus belong to the same cluster)
	 */
	@Override
	public Evaluation evaluate(Path path) {
		Node end = path.endNode();
		String sequence = (String) end.getProperty(SEQUENCE.name());
		String id = (String) end.getProperty(ID.name());

		if (!visited.contains(id)
				&& (path.startNode().getId() == path.endNode().getId()
				|| sequence.length() < threshold)) {
			visited.add(id);
			return Evaluation.INCLUDE_AND_CONTINUE;
		}
		return Evaluation.EXCLUDE_AND_PRUNE;
	}
}