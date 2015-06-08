package nl.tudelft.dnainator.graph;

import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Interface for backend agnostic interaction with a graph.
 */
public interface Graph {
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
	 * @return The {@link AnnotationCollection} containing the annotations.
	 */
	AnnotationCollection getAnnotations();

	/**
	 * Get all the nodes with a specific rank from this graph.
	 * @param rank	the rank
	 * @return		a list of sequence nodes
	 */
	List<SequenceNode> getRank(int rank);

	/**
	 * Get a list of all nodes from this graph.
	 * @return	a list of all nodes, per rank
	 */
	List<List<SequenceNode>> getRanks();

	/**
	 * Return a list of nodes that belong to the same cluster as the given startId.
	 * @param visited	nodes that were already visited
	 * @param startNodes	the start nodes
	 * @param threshold		the clustering threshold
	 * @return		a list representing the cluster
	 */
	Queue<Cluster> getClustersFrom(Set<String> visited, List<String> startNodes, int threshold);

	/**
	 * Return a list of nodes that belong to the same cluster as the given startId.
	 * @param startNodes	the start nodes
	 * @param end		the maximum rank of the cluster
	 * @param threshold	the clustering threshold
	 * @return		a list representing the cluster
	 */
	Map<Integer, List<Cluster>> getAllClusters(List<String> startNodes, int end, int threshold);

	/**
	 * Find the nodes satisfying the given query.
	 * @param q the query for finding the nodes.
	 * @return the result of the query.
	 */
	List<SequenceNode> queryNodes(GraphQueryDescription q);
}
