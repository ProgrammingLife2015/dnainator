package nl.tudelft.dnainator.graph;

import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Edge;

/**
 * Interface for backend agnostic interaction with a graph.
 */
public interface Graph extends GraphBuilder {
	/**
	 * Get the root node of this graph.
	 * FIXME: This is the node that has no incoming edges.
	 * @return	a SequenceNode
	 */
	SequenceNode getRootNode();

	/**
	 * Get the node with identifier n from this graph.
	 * @param n	the identifier
	 * @return	a SequenceNode
	 */
	SequenceNode getNode(String n);

	/**
	 * Find the nodes satisfying the given query.
	 * @param q the query for finding the nodes.
	 * @return the result of the query.
	 */
	List<SequenceNode> queryNodes(GraphQueryDescription q);

	/**
	 * Get a list of all nodes from this graph.
	 * @return	a list of all nodes, per rank
	 */
	List<List<SequenceNode>> getRanks();

	/**
	 * Get a list of all edges from this graph.
	 * @return	a list of all edges
	 */
	List<Edge<String>> getEdges();

	/**
	 * Get all the nodes with a specific rank from this graph.
	 * @param rank	the rank
	 * @return		a list of sequence nodes
	 */
	List<SequenceNode> getRank(int rank);
}
