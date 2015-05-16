package nl.tudelft.dnainator.graph.query;

/**
 * Describes the start rank to search from.
 * Forms a range together with {@link RankEnd}.
 */
public class RankStart implements QueryElement {
	private int start;

	/**
	 * Constructs the start rank.
	 * @param start The start rank number.
	 */
	public RankStart(int start) {
		this.start = start;
	}

	/**
	 * @return the start rank number.
	 */
	public int getStart() {
		return start;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}