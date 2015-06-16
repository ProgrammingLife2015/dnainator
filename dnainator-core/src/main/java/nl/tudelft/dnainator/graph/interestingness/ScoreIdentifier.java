package nl.tudelft.dnainator.graph.interestingness;

/**
 * A score identifier, which knows its name.
 */
public interface ScoreIdentifier {
	/**
	 * @return the fixed name of this score.
	 */
	String name();

	/**
	 * @return the pretty name of this score.
	 */
	String getName();
}
