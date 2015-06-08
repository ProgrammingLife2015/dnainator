package nl.tudelft.dnainator.graph.query;

/**
 * Describes the end rank to search towards.
 * Forms a range together with {@link RankStart}.
 */
public class RankEnd implements QueryElement {
	private int end;

	/**
	 * Constructs the end rank.
	 * @param end The end rank number.
	 */
	public RankEnd(int end) {
		this.end = end;
	}

	/**
	 * @return the end rank number.
	 */
	public int getEnd() {
		return end;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}