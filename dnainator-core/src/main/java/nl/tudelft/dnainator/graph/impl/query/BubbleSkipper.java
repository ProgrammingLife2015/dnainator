package nl.tudelft.dnainator.graph.impl.query;

import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.BranchState;

/**
 * A {@link PathExpander} which skips all nodes in bubbles on its path.
 */
public enum BubbleSkipper implements PathExpander<Object> {

	INSTANCE {
		@Override
		public Iterable<Relationship> expand(Path path, BranchState<Object> state) {
			Node from = path.endNode();
			if (from.hasLabel(NodeLabels.BUBBLE_SOURCE)) {
				return from.getRelationships(RelTypes.BUBBLE_SOURCE_OF, Direction.OUTGOING);
			} else {
				return from.getRelationships(RelTypes.NEXT, Direction.OUTGOING);
			}
		}

		@Override
		public PathExpander<Object> reverse() {
			throw new UnsupportedOperationException();
		}
	};

	/**
	 * @return The {@link BubbleSkipper} instance.
	 */
	public static BubbleSkipper get() {
		return INSTANCE;
	}
}
