package nl.tudelft.dnainator.graph.query;

public class RankStart implements QueryElement {
	private int start;

	public RankStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}