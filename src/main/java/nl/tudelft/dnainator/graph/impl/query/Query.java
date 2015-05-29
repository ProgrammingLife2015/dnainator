package nl.tudelft.dnainator.graph.impl.query;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The {@link Query} represents a query that can be executed on the database.
 * A query can return all sorts of things.
 * @param <T>	the returned type, inferred by Java or specified in implementing class
 */
public interface Query<T> {
	/**
	 * Execute this {@link Query} on the specified database service.
	 * @param service	the database service
	 * @return		the result of the query
	 */
	T execute(GraphDatabaseService service);
}
