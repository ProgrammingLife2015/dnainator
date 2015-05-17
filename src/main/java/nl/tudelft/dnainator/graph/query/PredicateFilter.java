package nl.tudelft.dnainator.graph.query;

import java.util.function.Predicate;

import nl.tudelft.dnainator.core.SequenceNode;

/**
 * Describes a predicate which each result element of the query
 * should satisfy.
 */
public class PredicateFilter implements QueryElement {
	private Predicate<SequenceNode> filter;

	/**
	 * Constructs a new predicate filter.
	 * @param p The predicate to use for filtering.
	 */
	public PredicateFilter(Predicate<SequenceNode> p) {
		filter = p;
	}

	/**
	 * @return the predicate to use for filtering.
	 */
	public Predicate<SequenceNode> getFilter() {
		return filter;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}