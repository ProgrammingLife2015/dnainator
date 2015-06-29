package nl.tudelft.dnainator.graph.impl.command;

import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

import java.util.HashSet;
import java.util.Set;

public class PhyloEvaluator implements Evaluator {
	private Set<Node> clusters = new HashSet<>();

	@Override
	public Evaluation evaluate(Path path) {
		if (path.endNode().hasLabel(NodeLabels.NODE)) {
			return Evaluation.EXCLUDE_AND_CONTINUE;
		} else if (path.endNode().hasLabel(NodeLabels.SOURCE)) {
			clusters.add(path.endNode());
			return Evaluation.EXCLUDE_AND_CONTINUE;
		}

		for (Relationship rel : path.endNode().getRelationships(Direction.OUTGOING,
									RelTypes.ANCESTOR_OF)) {
			if (!clusters.contains(rel.getEndNode())) {
				return Evaluation.INCLUDE_AND_PRUNE;
			}
		}

		clusters.add(path.endNode());
		return Evaluation.EXCLUDE_AND_CONTINUE;
	}

}
