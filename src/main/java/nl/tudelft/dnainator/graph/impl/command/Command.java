package nl.tudelft.dnainator.graph.impl.command;

import org.neo4j.graphdb.GraphDatabaseService;

public interface Command {
	void execute(GraphDatabaseService service);
}
