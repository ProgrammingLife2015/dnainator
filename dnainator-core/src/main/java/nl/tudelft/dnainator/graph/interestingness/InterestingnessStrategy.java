package nl.tudelft.dnainator.graph.interestingness;

/**
 * The {@link InterestingnessStrategy} determines how an interestingness score
 * is calculated.
 */
public interface InterestingnessStrategy {

	/**
	 * Compute the interestingness at the given location.
	 * @param container a {@link ScoreContainer} in the graph, which has
	 * several features.
	 * @return the interestingness score.
	 */
	int compute(ScoreContainer container);
}
