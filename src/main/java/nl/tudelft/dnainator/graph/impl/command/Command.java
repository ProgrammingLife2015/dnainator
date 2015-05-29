package nl.tudelft.dnainator.graph.impl.command;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The {@link Command} interface represents a command that can be executed on the database.
 * A {@link Command} does not return anything.
 */
public interface Command {
	/**
	 * Execute this {@link Command} on the specified database service.
	 * @param service	the database service
	 */
	void execute(GraphDatabaseService service);
}
