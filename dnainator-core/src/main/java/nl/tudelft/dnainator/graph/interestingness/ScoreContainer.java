package nl.tudelft.dnainator.graph.interestingness;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

	/**
	 * Returns all of the scores of this {@link ScoreContainer}. This uses getScore
	 * by default, but may be optimized by overriding it if the scores are stored
	 * internally as a {@link Map}.
	 * @return all of the scores of this {@link ScoreContainer}.
	 */
	default Map<ScoreIdentifier, Integer> getScores() {
		return Arrays.stream(Scores.values())
				.collect(Collectors.toMap(Function.identity(), this::getScore));
	}
}
