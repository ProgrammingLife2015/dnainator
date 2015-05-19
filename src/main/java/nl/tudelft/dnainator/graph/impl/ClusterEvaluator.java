package nl.tudelft.dnainator.graph.impl;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

/**
 * Evaluates whether a node is part of a cluster based on the given threshold.
 */
public class ClusterEvaluator implements Evaluator {
	private int threshold;

	/**
	 * Create an evaluator with the specified threshold.
	 * @param threshold	the specified threshold
	 */
	public ClusterEvaluator(int threshold) {
		this.threshold = threshold;
	}
	@Override
	public Evaluation evaluate(Path path) {
		Node end = path.endNode();
		String sequence = (String) end.getProperty("sequence");

		if (path.startNode().getId() == path.endNode().getId()
				|| sequence.length() < threshold) {
			return Evaluation.INCLUDE_AND_CONTINUE;
		}
		return Evaluation.EXCLUDE_AND_PRUNE;
	}
}