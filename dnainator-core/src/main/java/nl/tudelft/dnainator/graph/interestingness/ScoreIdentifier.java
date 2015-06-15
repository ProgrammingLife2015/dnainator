package nl.tudelft.dnainator.graph.interestingness;

/**
 * A score identifier, which knows its name.
 */
public interface ScoreIdentifier {

	/**
	 * @return the name of this score.
	 */
	String getName();

	/**
	 * Modifies the given raw score to how important it is.
	 * @param rawScore the raw statistics of a score.
	 * @return the score, modified to how important it is.
	 */
	int applyImportanceModifier(int rawScore);
}
