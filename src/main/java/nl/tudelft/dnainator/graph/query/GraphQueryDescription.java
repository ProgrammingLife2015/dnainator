package nl.tudelft.dnainator.graph.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import nl.tudelft.dnainator.core.SequenceNode;

/**
 * A description of a query and its parameters.
 */
public class GraphQueryDescription {
	private Collection<QueryElement> elems = new ArrayList<>();

	/**
	 * Query for the given id.
	 * @param id the String id to search for
	 * @return this
	 */
	public GraphQueryDescription hasId(String id) {
		return haveIds(Collections.singletonList(id));
	}

	/**
	 * Query for the given ids.
	 * @param ids the ids to search for.
	 * @return this
	 */
	public GraphQueryDescription haveIds(Collection<String> ids) {
		elems.add(new IDsFilter(ids));
		return this;
	}

	/**
	 * The result should have the given source as one of its sources.
	 * @param source The source to search for.
	 * @return this
	 */
	public GraphQueryDescription containsSource(String source) {
		return containsSources(Collections.singletonList(source));
	}

	/**
	 * The result should have one of the given sources.
	 * @param sources The sources to search for.
	 * @return this
	 */
	public GraphQueryDescription containsSources(Collection<String> sources) {
		elems.add(new SourcesFilter(sources));
		return this;
	}

	/**
	 * Filter the result with the given {@link Predicate}.
	 * @param p the predicate to test against.
	 * @return this
	 */
	public GraphQueryDescription filter(Predicate<SequenceNode> p) {
		elems.add(new PredicateFilter(p));
		return this;
	}

	/**
	 * Search from the given rank (inclusive).
	 * @param start The rank to search from.
	 * @return this
	 */
	public GraphQueryDescription fromRank(int start) {
		elems.add(new RankStart(start));
		return this;
	}

	/**
	 * Search towards the given rank (exclusive).
	 * @param end The rank to search towards.
	 * @return this
	 */
	public GraphQueryDescription toRank(int end) {
		elems.add(new RankEnd(end));
		return this;
	}

	public void accept(GraphQuery q) {
		for (QueryElement e : elems) {
			e.accept(q);
		}
	}
}