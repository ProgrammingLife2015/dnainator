package nl.tudelft.dnainator.graph;

import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;

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
	 * Get all the nodes with a specific rank from this graph.
	 * @param rank	the rank
	 * @return		a list of sequence nodes
	 */
	List<SequenceNode> getRank(int rank);
}
