package nl.tudelft.dnainator.graph.query;

import java.util.Collection;

public class IDsFilter implements QueryElement {
	private Collection<String> ids;

	public IDsFilter(Collection<String> ids) {
		this.ids = ids;
	}

	public Collection<String> getIds() {
		return ids;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}