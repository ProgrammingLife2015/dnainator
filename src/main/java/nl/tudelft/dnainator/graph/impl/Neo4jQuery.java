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
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;

/**
 * A useful class for creating and executing a query on a Neo4j
 * database using a {@link GraphQueryDescription}.
 */
public class Neo4jQuery {
	private String cypherQuery;
	private boolean multipleConditions = false;
	private Map<String, Object> parameters;
	private GraphQueryDescription description;

	/**
	 * Create a new query suitable for Neo4j from the given description.
	 * @param qd the query description to use for constructing the query.
	 */
	public Neo4jQuery(GraphQueryDescription qd) {
		this.description = qd;
		buildQuery();
	}

	private void addCondition(StringBuilder sb, String c) {
		if (multipleConditions) {
			sb.append("AND ");
		} else {
			sb.append("WHERE ");
			// All next conditions are AND-ed
			multipleConditions = true;
		}
		sb.append(c);
	}

	private void buildQuery() {
		parameters = new HashMap<>();
		StringBuilder query = new StringBuilder("MATCH n\n");
		if (description.shouldQueryIds()) {
			addCondition(query, "n.id IN {ids}\n");
			parameters.put("ids", description.getIds());
		}
		if (description.shouldQuerySources()) {
			addCondition(query, "n.source IN {sources}\n");
			parameters.put("sources", description.getSources());
		}
		if (description.shouldQueryFrom()) {
			addCondition(query, "n.dist >= {from}\n");
			parameters.put("from", description.getFrom());
		}
		if (description.shouldQueryTo()) {
			addCondition(query, "n.dist < {to}\n");
			parameters.put("to", description.getTo());
		}
		query.append("RETURN n");
		cypherQuery = query.toString();
	}

	/**
	 * Execute the query on the given database.
	 * @param db the database to execute the query on.
	 * @return the query result.
	 */
	public List<SequenceNode> execute(GraphDatabaseService db) {
		List<SequenceNode> result;
		Predicate<SequenceNode> p;
		if (description.shouldFilter()) {
			p = description.getFilter();
		} else {
			p = (sn) -> true;
		}
		try (Transaction tx = db.beginTx()) {
			Result r = db.execute(cypherQuery, parameters);
			ResourceIterator<Node> it = r.columnAs("n");
			result = IteratorUtil.asCollection(it).stream()
				.map(Neo4jGraph::createSequenceNode)
				.filter(p)
				.collect(Collectors.toList());
			tx.success();
		}
		return result;
	}
}
