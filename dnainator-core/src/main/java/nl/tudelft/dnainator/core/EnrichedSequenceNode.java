package nl.tudelft.dnainator.core;

import java.util.List;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.graph.interestingness.ScoreContainer;

/**
 * An enriched {@link SequenceNode}, which is enriched with respect to an ordinary
 * {@link SequenceNode} because it also contains outgoing edges, its rank within
 * the graph, scores and annotations.
 */
public interface EnrichedSequenceNode extends SequenceNode, ScoreContainer {

	/**
	 * The associated annotations.
	 * @return	the annotations
	 */
	List<Annotation> getAnnotations();

	/**
	 * The associated incoming neighbours.
	 * @return	a list of neighbour id's
	 */
	List<String> getOutgoing();

	/**
	 * The associated maximum distance from the origin in base pairs.
	 * @return	the distance in base pairs
	 */
	int getBaseDistance();

	/**
	 * The associated rank.
	 * @return	the rank
	 */
	int getRank();

	/**
	 * @return The interestingness score of this node, at the moment it
	 * was retrieved from the graph.
	 */
	int getInterestingnessScore();
}