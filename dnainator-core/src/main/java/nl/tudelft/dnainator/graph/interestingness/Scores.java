package nl.tudelft.dnainator.graph.interestingness;

/**
 * All of the supported scores.
 */
public enum Scores implements ScoreIdentifier {
	SEQ_LENGTH("seqLength"),
	INDEP_MUT("independentMutation");

	private String name;

	/**
	 * For constructing a score with the given name.
	 * @param name the name of the score.
	 */
	Scores(String name) {
		this.name = name;
	}

	@Override
	public String description() {
		return name;
	}
}
