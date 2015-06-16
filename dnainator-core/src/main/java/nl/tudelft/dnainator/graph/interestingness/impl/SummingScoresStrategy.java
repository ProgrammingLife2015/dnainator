package nl.tudelft.dnainator.graph.interestingness.impl;

import nl.tudelft.dnainator.graph.interestingness.InterestingnessStrategy;
import nl.tudelft.dnainator.graph.interestingness.ScoreContainer;
import nl.tudelft.dnainator.graph.interestingness.ScoreIdentifier;
import nl.tudelft.dnainator.graph.interestingness.Scores;

/**
 * An {@link InterestingnessStrategy} which simply sums all statically available
 * {@link Scores} together.
 */
public class SummingScoresStrategy implements InterestingnessStrategy {
	private static final int RANGE_LOWER = 0;
	private static final int RANGE_UPPER = 1000;

	@Override
	public int compute(ScoreContainer container) {
		int sum = 0;
		for (ScoreIdentifier id : Scores.values()) {
			sum += id.applyImportanceModifier(container.getScore(id));
		}
		// Clamp between 0 and 1000.
		return Math.max(RANGE_LOWER, Math.min(sum, RANGE_UPPER));
	}

}
