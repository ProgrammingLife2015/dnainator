package nl.tudelft.dnainator.graph.interestingness;

/**
 * All of the supported scores.
 */
public enum Scores implements ScoreIdentifier {

	SEQ_LENGTH("seqLength") {
		private static final int DENOMINATOR = 500;
		private static final int SEQ_LENGTH_THRESHOLD = 5000;

		@Override
		public int applyImportanceModifier(int rawScore) {
			if (rawScore < SEQ_LENGTH_THRESHOLD) {
				return rawScore / DENOMINATOR;
			} else {
				return (rawScore * rawScore) / DENOMINATOR;
			}
		}
	},
	INDEP_MUT("independentMutation") {
		private static final int BASE = 8;
		@Override
		public int applyImportanceModifier(int rawScore) {
			return (int) Math.pow(BASE, rawScore);
		}
	};

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
