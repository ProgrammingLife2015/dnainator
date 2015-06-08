package nl.tudelft.dnainator.graph.impl.query;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

import java.util.Set;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ID;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.SCORE;

/**
 * Evaluates whether a node is part of a cluster based on the given threshold.
 */
public class ClusterEvaluator implements Evaluator {
	private int threshold;
	private Set<String> visited;

	/**
	 * Create a new {@link ClusterEvaluator}, which will:.
	 * <ul>
	 *   <li>only cluster nodes that haven't been visited yet</li>
	 *   <li>use the specified threshold</li>
	 * </ul>
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
	 * <ul>
	 *   <li>haven't been visited yet and</li>
	 *   <li>are the start node
	 *   <ul>
	 *     <li>have a sequence &lt; threshold (and thus belong to the same cluster)</li>
	 *   </ul>
	 * </ul>
	 */
	@Override
	public Evaluation evaluate(Path path) {
		Node end = path.endNode();
		int score = (int) end.getProperty(SCORE.name());
		String id = (String) end.getProperty(ID.name());

		if (!visited.contains(id)
				&& (path.startNode().getId() == path.endNode().getId()
				|| score < threshold)) {
			visited.add(id);
			return Evaluation.INCLUDE_AND_CONTINUE;
		}
		return Evaluation.EXCLUDE_AND_PRUNE;
	}
}