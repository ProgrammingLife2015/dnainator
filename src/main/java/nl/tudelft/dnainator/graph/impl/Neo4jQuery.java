package nl.tudelft.dnainator.graph.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.graph.query.GraphQuery;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;
import nl.tudelft.dnainator.graph.query.IDsFilter;
import nl.tudelft.dnainator.graph.query.PredicateFilter;
import nl.tudelft.dnainator.graph.query.RankEnd;
import nl.tudelft.dnainator.graph.query.RankStart;
import nl.tudelft.dnainator.graph.query.SourcesFilter;

/**
 * A useful class for creating and executing a query on a Neo4j
 * database using a {@link GraphQueryDescription}.
 */
public class Neo4jQuery implements GraphQuery {
	private boolean multipleConditions = false;
	private Map<String, Object> parameters;
	private StringBuilder sb;
	private Predicate<SequenceNode> p;

	/**
	 * Create a new query suitable for Neo4j from the given description.
	 * @param qd the query description to use for constructing the query.
	 */
	public Neo4jQuery(GraphQueryDescription qd) {
		this.sb = new StringBuilder("MATCH n\n");
		this.parameters = new HashMap<>();
		this.p = (sn) -> true;
		compile(qd);
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
				.map(Neo4jGraph::createSequenceNode)
				.filter(p)
				.collect(Collectors.toList());
			tx.success();
		}
		return result;
	}

	@Override
	public void compile(GraphQueryDescription qd) {
		qd.accept(this);
	}

	@Override
	public void compile(IDsFilter ids) {
		addCondition("n.id IN {ids}\n");
		parameters.put("ids", ids.getIds());
	}

	@Override
	public void compile(SourcesFilter sources) {
		addCondition("n.source IN {sources}\n");
		parameters.put("sources", sources.getSources());
	}

	@Override
	public void compile(PredicateFilter predicate) {
		this.p = predicate.getFilter();
	}

	@Override
	public void compile(RankStart start) {
		addCondition("n.dist >= {from}\n");
		parameters.put("from", start.getStart());
	}

	@Override
	public void compile(RankEnd end) {
		addCondition("n.dist < {to}\n");
		parameters.put("to", end.getEnd());
	}
}
