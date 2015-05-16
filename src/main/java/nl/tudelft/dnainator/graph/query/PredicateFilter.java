package nl.tudelft.dnainator.graph.query;

import java.util.function.Predicate;

import nl.tudelft.dnainator.core.SequenceNode;

public class PredicateFilter implements QueryElement {
	private Predicate<SequenceNode> filter;

	public PredicateFilter(Predicate<SequenceNode> p) {
		filter = p;
	}

	public Predicate<SequenceNode> getFilter() {
		return filter;
	}

	@Override
	public void accept(GraphQuery q) {
		q.compile(this);
	}

}