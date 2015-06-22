package nl.tudelft.dnainator.graph.impl.command;

import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;

import java.util.LinkedList;
import java.util.List;

/**
 * PathExpander for determining the topological ordering.
 */
public class TopologicalPathExpander implements PathExpander<Object> {
	private static final String PROCESSED = "PROCESSED";

	private boolean hasUnprocessedIncoming(Node n) {
		Iterable<Relationship> in = n.getRelationships(RelTypes.NEXT, Direction.INCOMING);
		for (Relationship r : in) {
			if (!r.hasProperty(PROCESSED)) {
				return true;
			}
		}
		// Clean up after ourselves.
		in.forEach(rel -> rel.removeProperty(PROCESSED));
		// All incoming edges have been processed.
		return false;
	}

	@Override
	public Iterable<Relationship> expand(Path path,
			BranchState<Object> noState) {
		Node from = path.endNode();
		List<Relationship> expand = new LinkedList<>();
		for (Relationship out : from.getRelationships(RelTypes.NEXT, Direction.OUTGOING)) {
			setNumStrainsThrough(out);
			out.setProperty(PROCESSED, true);
			if (!hasUnprocessedIncoming(out.getEndNode())) {
				// All of the dependencies of this node have been added to the result.
				expand.add(out);
			}
		}
		return expand;
	}

	private void setNumStrainsThrough(Relationship r) {
		r.setProperty(SequenceProperties.EDGE_NUM_STRAINS.name(), Math.abs(
				r.getStartNode().getDegree(RelTypes.SOURCE)
				- r.getEndNode().getDegree(RelTypes.SOURCE)));
	}

	@Override
	public PathExpander<Object> reverse() {
		throw new UnsupportedOperationException();
	}

}