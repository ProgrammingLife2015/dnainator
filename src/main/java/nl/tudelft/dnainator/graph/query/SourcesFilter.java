package nl.tudelft.dnainator.graph.query;

import java.util.Collection;

public class SourcesFilter implements QueryElement {
	private Collection<String> sources;

	public SourcesFilter(Collection<String> sources) {
		this.sources = sources;
	}

	public Collection<String> getSources() {
		return sources;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}