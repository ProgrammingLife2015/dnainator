package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.graph.interestingness.ScoreContainer;
import nl.tudelft.dnainator.graph.interestingness.ScoreIdentifier;

import org.neo4j.graphdb.Node;

/**
 * Retrieves the score properties from a {@link Node}. Must be done
 * within a transaction.
 */
public class Neo4jScoreContainer implements ScoreContainer {
	private Node delegate;

	/**
	 * Constructs a new Location, delegating to the given path.
	 * @param delegate the path which has nodes and edges with certain
	 * properties.
	 */
	public Neo4jScoreContainer(Node delegate) {
		this.delegate = delegate;
	}

	@Override
	public int getScore(ScoreIdentifier id) {
		// Get's the score, cast to an int. Default value of 0.
		return (int) delegate.getProperty(id.getName(), 0);
	}

}
