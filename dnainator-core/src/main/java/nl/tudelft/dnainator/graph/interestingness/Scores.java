package nl.tudelft.dnainator.graph.interestingness;

/**
 * All of the supported scores.
 */
public enum Scores implements ScoreIdentifier {

	SEQ_LENGTH("seqLength");

	private String name;

	/**
	 * For constructing a score with the given name.
	 * @param name the name of the score.
	 */
	Scores(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
