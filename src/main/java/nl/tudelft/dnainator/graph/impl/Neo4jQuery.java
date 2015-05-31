package nl.tudelft.dnainator.graph.impl;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ID;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.RANK;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.SOURCE;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.graph.impl.query.NodeQuery;
import nl.tudelft.dnainator.graph.query.GraphQuery;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;
import nl.tudelft.dnainator.graph.query.IDsFilter;
import nl.tudelft.dnainator.graph.query.PredicateFilter;
import nl.tudelft.dnainator.graph.query.RankEnd;
import nl.tudelft.dnainator.graph.query.RankStart;
import nl.tudelft.dnainator.graph.query.SourcesFilter;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

/**
 * A useful class for creating and executing a query on a Neo4j
 * database using a {@link GraphQueryDescription}.
 */
public final class Neo4jQuery implements GraphQuery {
	private boolean multipleConditions;
	private Map<String, Object> parameters;
	private StringBuilder sb;
	private Predicate<SequenceNode> p;

	private Neo4jQuery() {
		this.sb = new StringBuilder("MATCH n-[" + SOURCE.name() + "]->p\n");
	}

	private void addCondition(String c) {
		if (multipleConditions) {
			sb.append("AND ");
		} else {
			sb.append("WHERE ");
			// All next conditions are AND-ed
			multipleConditions = true;
		}
		sb.append(c);
	}

	/**
	 * Execute the query on the given database.
	 * @param db the database to execute the query on.
	 * @return the query result.
	 */
	public List<SequenceNode> execute(GraphDatabaseService db) {
		sb.append("RETURN n");
		List<SequenceNode> result;
		try (Transaction tx = db.beginTx()) {
			Result r = db.execute(sb.toString(), parameters);
			ResourceIterator<Node> it = r.columnAs("n");
			result = IteratorUtil.asCollection(it).stream()
				.map(e -> new NodeQuery(e).execute(db))
				.filter(p)
				.collect(Collectors.toList());
			tx.success();
		}
		return result;
	}

	@Override
	public void compile(GraphQueryDescription qd) {
		this.multipleConditions = false;
		this.parameters = new HashMap<>();
		this.p = (sn) -> true;
		qd.accept(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compile(IDsFilter ids) {
		parameters.compute("ids", (k, v) -> {
			if (v == null) {
				addCondition("n." + ID.name() + " IN {ids}\n");
				return ids.getIds();
			}
			((Collection<String>) v).addAll(ids.getIds());
			return v;
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compile(SourcesFilter sources) {
		parameters.compute("sources", (k, v) -> {
			if (v == null) {
				addCondition("p." + SOURCE.name() + " IN {sources}\n");
				return sources.getSources();
			}
			((Collection<String>) v).addAll(sources.getSources());
			return v;
		});
	}

	@Override
	public void compile(PredicateFilter predicate) {
		this.p = predicate.getFilter();
	}

	@Override
	public void compile(RankStart start) {
		addCondition("n." + RANK.name() + " >= {from}\n");
		parameters.put("from", start.getStart());
	}

	@Override
	public void compile(RankEnd end) {
		addCondition("n." + RANK.name() + " < {to}\n");
		parameters.put("to", end.getEnd());
	}

	/**
	 * Construct a {@link Neo4jQuery} based on the given
	 * {@link GraphQueryDescription}.
	 * @param qd the description of the query.
	 * @return the compiled query.
	 */
	public static Neo4jQuery of(GraphQueryDescription qd) {
		Neo4jQuery q = new Neo4jQuery();
		q.compile(qd);
		return q;
	}
}
