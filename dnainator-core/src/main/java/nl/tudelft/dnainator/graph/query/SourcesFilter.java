package nl.tudelft.dnainator.graph.query;

import java.util.Collection;

/**
 * Describes a filter on sources.
 */
public class SourcesFilter implements QueryElement {
	private Collection<String> sources;

	/**
	 * Constructs a new filter on sources.
	 * @param sources The sources to filter on.
	 */
	public SourcesFilter(Collection<String> sources) {
		this.sources = sources;
	}

	/**
	 * @return the sources to filter on.
	 */
	public Collection<String> getSources() {
		return sources;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}