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

	@Override
	public int compute(ScoreContainer container) {
		int sum = 0;
		for (ScoreIdentifier id : Scores.values()) {
			sum += container.getScore(id);
		}
		return sum;
	}

}
