package nl.tudelft.dnainator.graph.interestingness;

/**
 * The {@link InterestingnessStrategy} determines how an interestingness score
 * is calculated.
 */
public interface InterestingnessStrategy {
	int RANGE_LOWER = 0;
	int RANGE_UPPER = 1000;

	/**
	 * Compute the interestingness at the given location.
	 * @param container a {@link ScoreContainer} in the graph, which has
	 * several features.
	 * @return the interestingness score, within the range of 0 to 1000.
	 */
	int compute(ScoreContainer container);
}
