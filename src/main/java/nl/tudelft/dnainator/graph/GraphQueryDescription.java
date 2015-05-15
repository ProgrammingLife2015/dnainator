package nl.tudelft.dnainator.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import nl.tudelft.dnainator.core.SequenceNode;

public class GraphQueryDescription {
	private Collection<String> idStrings;
	private Collection<String> sourceStrings;
	private Predicate<SequenceNode> filter;
	private boolean queryFrom = false;
	private int from = 0;
	private boolean queryTo = false;
	private int to = Integer.MAX_VALUE;

	public GraphQueryDescription hasId(String id) {
		return haveIds(Collections.singletonList(id));
	}

	public GraphQueryDescription haveIds(Collection<String> ids) {
		if (idStrings == null) {
			idStrings = new ArrayList<>(ids);
			return this;
		}
		idStrings.addAll(ids);
		return this;
	}

	public boolean shouldQueryIds() {
		return idStrings != null;
	}

	public Collection<String> getIds() {
		return idStrings;
	}

	public GraphQueryDescription containsSource(String source) {
		return containsSources(Collections.singletonList(source));
	}

	public GraphQueryDescription containsSources(Collection<String> sources) {
		if (sourceStrings == null) {
			sourceStrings = new ArrayList<>(sources);
			return this;
		}
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
