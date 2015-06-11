package nl.tudelft.dnainator.graph.interestingness;

/**
 * Represents a location within the graph.
 */
public interface ScoreContainer {

	/**
	 * @param id the identifier of the property, which is used to get the score of
	 * a particular property.
	 * @return the score of a particular property
	 */
	int getScore(ScoreIdentifier id);
}
