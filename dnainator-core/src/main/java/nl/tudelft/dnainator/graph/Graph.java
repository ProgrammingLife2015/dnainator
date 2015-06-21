package nl.tudelft.dnainator.graph;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.Range;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.core.impl.TreeNode;
import nl.tudelft.dnainator.graph.interestingness.InterestingnessStrategy;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface for backend agnostic interaction with a graph.
 */
public interface Graph extends AnnotationCollection {
	/**
	 * Get the root node of this graph.
	 * FIXME: This is the node that has no incoming edges.
	 * @return	a SequenceNode
	 */
	EnrichedSequenceNode getRootNode();

	/**
	 * Get the node with identifier n from this graph.
	 * @param n	the identifier
	 * @return	a SequenceNode
	 */
	EnrichedSequenceNode getNode(String n);

	/**
	 * @return The {@link AnnotationCollection} containing the annotations.
	 */
	AnnotationCollection getAnnotations();

	/**
	 * @return The root {@link TreeNode}.
	 */
	TreeNode getTree();

	/**
	 * Get all the nodes with a specific rank from this graph.
	 * @param rank	the rank
	 * @return		a list of sequence nodes
	 */
	List<EnrichedSequenceNode> getRank(int rank);

	/**
	 * Return the maximum rank in this graph.
	 * @return	the maximum rank
	 */
	int getMaxRank();

	/**
	 * Return the maximum amount of base pairs in this graph.
	 * @return  the maximum amount of base pairs
	 */
	int getMaxBasePairs();

	/**
	 * Return the rank belonging to the given base pair.
	 * @param base  the base pair whose rank to find
	 * @return  the rank beloning to the given base pair
	 * */
	int getRankFromBasePair(int base);

	/**
	 * Return a list of nodes that belong to the same cluster as the given startId.
	 * @param startNodes	the start nodes
	 * @param end		the maximum rank of the cluster
	 * @param threshold	the clustering threshold
	 * @return		a list representing the cluster
	 */
	Map<Integer, List<Cluster>> getAllClusters(List<String> startNodes, int end, int threshold);

	/**
	 * Sets the interestingness strategy which calculates the interestingness when
	 * clustering.
	 * @param is the interestingness strategy.
	 */
	void setInterestingnessStrategy(InterestingnessStrategy is);

	/**
	 * Find the nodes satisfying the given query.
	 * @param q the query for finding the nodes.
	 * @return the result of the query.
	 */
	List<EnrichedSequenceNode> queryNodes(GraphQueryDescription q);

	/**
	 * Return all annotations covered by the given range of ranks.
	 * @param r The range
	 * @return all annotations covered.
	 */
	Collection<Annotation> getAnnotationByRank(Range r);
}
