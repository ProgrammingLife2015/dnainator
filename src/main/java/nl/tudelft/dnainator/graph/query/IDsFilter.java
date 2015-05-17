package nl.tudelft.dnainator.graph.query;

import java.util.Collection;

/**
 * Describes a filter on IDs.
 */
public class IDsFilter implements QueryElement {
	private Collection<String> ids;

	/**
	 * Constructs a new filter.
	 * @param ids The String IDs to filter on.
	 */
	public IDsFilter(Collection<String> ids) {
		this.ids = ids;
	}

	/**
	 * @return the String IDs to filter on.
	 */
	public Collection<String> getIds() {
		return ids;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}