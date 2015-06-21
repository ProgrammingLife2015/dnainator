package nl.tudelft.dnainator.graph.impl.query;

import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

/**
 * Makes the traverser go up to a certain rank, and manages uniqueness of nodes.
 */
public class UntilRankEvaluator implements Evaluator {
	private int endRank;

	/**
	 * Constructs a new {@link UntilRankEvaluator}.
	 * @param endRank the rank to stop at.
	 */
	public UntilRankEvaluator(int endRank) {
		this.endRank = endRank;
	}

	private int getRank(Node n) {
		return (int) n.getProperty(SequenceProperties.RANK.name());
	}

	@Override
	public Evaluation evaluate(Path path) {
		Node from = path.endNode();
		if (getRank(from) <= endRank) {
			return Evaluation.INCLUDE_AND_CONTINUE;
		} else {
			return Evaluation.EXCLUDE_AND_PRUNE;
		}
	}

}
