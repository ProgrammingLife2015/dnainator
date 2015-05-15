package nl.tudelft.dnainator.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import nl.tudelft.dnainator.core.SequenceNode;

public class GraphQueryDescription {
	private Collection<Integer> idIntegers;
	private Collection<String> sourceStrings;
	private Predicate<SequenceNode> filter;
	private boolean queryFrom = false;
	private int from = 0;
	private boolean queryTo = false;
	private int to = Integer.MAX_VALUE;

	public GraphQueryDescription hasId(int id) {
		return haveIds(Collections.singletonList(id));
	}

	public GraphQueryDescription haveIds(Collection<Integer> ids) {
		idIntegers.addAll(ids);
		return this;
	}

	public boolean shouldQueryIds() {
		return idIntegers != null;
	}

	public Collection<Integer> getIds() {
		return idIntegers;
	}

	public GraphQueryDescription containsSource(String source) {
		return containsSources(Collections.singletonList(source));
	}

	public GraphQueryDescription containsSources(Collection<String> sources) {
		sourceStrings.addAll(sources);
		return this;
	}

	public boolean shouldQuerySources() {
		return sourceStrings != null;
	}

	public Collection<String> getSources() {
		return sourceStrings;
	}

	public GraphQueryDescription filter(Predicate<SequenceNode> p) {
		filter = p;
		return this;
	}

	public boolean shouldFilter() {
		return filter != null;
	}

	public Predicate<SequenceNode> getFilter() {
		return filter;
	}

	public GraphQueryDescription fromRank(int start) {
		from = start;
		queryFrom = true;
		return this;
	}

	public boolean shouldQueryFrom() {
		return queryFrom;
	}

	public int getFrom() {
		return from;
	}

	public GraphQueryDescription toRank(int end) {
		to = end;
		queryTo = true;
		return this;
	}

	public boolean shouldQueryTo() {
		return queryTo;
	}

	public int getTo() {
		return to;
	}
}
