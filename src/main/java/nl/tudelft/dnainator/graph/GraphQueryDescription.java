package nl.tudelft.dnainator.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import nl.tudelft.dnainator.core.SequenceNode;

/**
 * A description of a query and its parameters.
 */
public class GraphQueryDescription {
	private Collection<String> idStrings;
	private Collection<String> sourceStrings;
	private Predicate<SequenceNode> filter;
	private boolean queryFrom = false;
	private int from = 0;
	private boolean queryTo = false;
	private int to = Integer.MAX_VALUE;

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
		if (idStrings == null) {
			idStrings = new ArrayList<>(ids);
			return this;
		}
		idStrings.addAll(ids);
		return this;
	}

	/**
	 * @return Whether the query should query for ids.
	 */
	public boolean shouldQueryIds() {
		return idStrings != null;
	}

	/**
	 * @return The ids to query for.
	 */
	public Collection<String> getIds() {
		return idStrings;
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
		if (sourceStrings == null) {
			sourceStrings = new ArrayList<>(sources);
			return this;
		}
		sourceStrings.addAll(sources);
		return this;
	}

	/**
	 * @return Whether the query should query for sources.
	 */
	public boolean shouldQuerySources() {
		return sourceStrings != null;
	}

	/**
	 * @return The source parameters.
	 */
	public Collection<String> getSources() {
		return sourceStrings;
	}

	/**
	 * Filter the result with the given {@link Predicate}.
	 * @param p the predicate to test against.
	 * @return this
	 */
	public GraphQueryDescription filter(Predicate<SequenceNode> p) {
		filter = p;
		return this;
	}

	/**
	 * @return Whether the result should be filtered.
	 */
	public boolean shouldFilter() {
		return filter != null;
	}

	/**
	 * @return The filter predicate.
	 */
	public Predicate<SequenceNode> getFilter() {
		return filter;
	}

	/**
	 * Search from the given rank (inclusive).
	 * @param start The rank to search from.
	 * @return this
	 */
	public GraphQueryDescription fromRank(int start) {
		from = start;
		queryFrom = true;
		return this;
	}

	/**
	 * @return Whether the query should search from a rank.
	 */
	public boolean shouldQueryFrom() {
		return queryFrom;
	}

	/**
	 * @return The rank to search from (inclusive).
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * Search towards the given rank (exclusive).
	 * @param end The rank to search towards.
	 * @return this
	 */
	public GraphQueryDescription toRank(int end) {
		to = end;
		queryTo = true;
		return this;
	}

	/**
	 * @return Whether the query should search towards a rank.
	 */
	public boolean shouldQueryTo() {
		return queryTo;
	}

	/**
	 * @return The rank to search towards (exclusive).
	 */
	public int getTo() {
		return to;
	}
}
