package nl.tudelft.dnainator.graph.impl;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Utility methods for neo4j nodes and relationships.
 */
public final class Neo4jUtil {

	private Neo4jUtil() {
		
	}

	/**
	 * @param n the {@link Node}
	 * @return the number of incoming NEXT relationships.
	 */
	public static int inDegree(Node n) {
		return n.getDegree(RelTypes.NEXT, Direction.INCOMING);
	}

	/**
	 * @param n the {@link Node}
	 * @return the number of outgoing NEXT relationships.
	 */
	public static int outDegree(Node n) {
		return n.getDegree(RelTypes.NEXT, Direction.OUTGOING);
	}

	/**
	 * @param n the {@link Node}
	 * @return the outgoing NEXT relationships of the node.
	 */
	public static Iterable<Relationship> outgoing(Node n) {
		return n.getRelationships(RelTypes.NEXT, Direction.OUTGOING);
	}

	/**
	 * @param n the {@link Node}
	 * @return the incoming NEXT relationships of the node.
	 */
	public static Iterable<Relationship> incoming(Node n) {
		return n.getRelationships(RelTypes.NEXT, Direction.INCOMING);
	}
}
