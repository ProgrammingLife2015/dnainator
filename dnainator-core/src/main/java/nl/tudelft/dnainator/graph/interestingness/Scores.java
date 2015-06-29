package nl.tudelft.dnainator.graph.interestingness;

/**
 * All of the supported scores.
 */
public enum Scores implements ScoreIdentifier {
	SEQ_LENGTH("Sequence Length Score") {
		private static final int SEQ_LENGTH_THRESHOLD = 799;
		private static final int SEQ_LENGTH_MULTIPLIER = 3;

		@Override
		public int applyImportanceModifier(int rawScore) {
			return Math.min(SEQ_LENGTH_THRESHOLD, rawScore * SEQ_LENGTH_MULTIPLIER);
		}
	},
	DR_MUT("Drug Resistance Mutation Score") {
		private final int[] multipliers = { 0, 800, 1000 };
		@Override
		public int applyImportanceModifier(int rawScore) {
			if (rawScore >= multipliers.length) {
				return multipliers[multipliers.length - 1];
			}
			return multipliers[rawScore];
		}
	},
	INDEP_MUT("independentMutation") {
		@Override
		public int applyImportanceModifier(int rawScore) {
			return rawScore * 100;
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
	public String description() {
		return name;
	}
}
