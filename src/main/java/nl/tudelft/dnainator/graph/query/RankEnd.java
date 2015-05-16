package nl.tudelft.dnainator.graph.query;

public class RankEnd implements QueryElement {
	private int end;

	public RankEnd(int end) {
		this.end = end;
	}

	public int getEnd() {
		return end;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}