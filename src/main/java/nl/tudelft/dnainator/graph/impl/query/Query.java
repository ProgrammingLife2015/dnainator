package nl.tudelft.dnainator.graph.impl.query;

import org.neo4j.graphdb.GraphDatabaseService;

public interface Query<T> {
	T execute(GraphDatabaseService service);
}
