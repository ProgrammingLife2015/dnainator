package nl.tudelft.dnainator.graph.interestingness;

import nl.tudelft.dnainator.core.PropertyType;

/**
 * A score identifier, which knows its name.
 */
public interface ScoreIdentifier extends PropertyType {
	/**
	 * Modifies the given raw score to how important it is.
	 * @param rawScore the raw statistics of a score.
	 * @return the score, modified to how important it is.
	 */
	int applyImportanceModifier(int rawScore);
}
