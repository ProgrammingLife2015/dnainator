package nl.tudelft.dnainator.graph.impl.command;

import nl.tudelft.dnainator.graph.impl.RelTypes;
import org.neo4j.collection.primitive.PrimitiveLongSet;
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
public class TopologicalPathExpander implements PathExpander<PrimitiveLongSet> {
	private boolean hasUnprocessedIncoming(PrimitiveLongSet processed, Node n) {
		Iterable<Relationship> in = n.getRelationships(RelTypes.NEXT, Direction.INCOMING);
		for (Relationship r : in) {
			if (!processed.contains(r.getId())) {
				return true;
			}
		}
		// All incoming edges have been processed.
		return false;
	}

	@Override
	public Iterable<Relationship> expand(Path path,
			BranchState<PrimitiveLongSet> state) {
		Node from = path.endNode();
		List<Relationship> expand = new LinkedList<>();
		for (Relationship r : from.getRelationships(RelTypes.NEXT, Direction.OUTGOING)) {
			PrimitiveLongSet processed = state.getState();
			processed.add(r.getId());
			if (!hasUnprocessedIncoming(processed, r.getEndNode())) {
				// All of the dependencies of this node have been added to the result.
				expand.add(r);
			}
		}
		return expand;
	}

	@Override
	public PathExpander<PrimitiveLongSet> reverse() {
		throw new UnsupportedOperationException();
	}

}