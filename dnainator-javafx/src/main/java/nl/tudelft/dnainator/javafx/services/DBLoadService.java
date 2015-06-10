package nl.tudelft.dnainator.javafx.services;

import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

/**
 * A JavaFX service that loads an existing neo4j database based on its directory path.
 */
public class DBLoadService extends Service<Graph> {
	private static final String DEFAULT_DB_PATH = "target/db";
	private ObjectProperty<String> database = new SimpleObjectProperty<>(this, "database");

	/**
	 * Construct a DBLoadService with a default database path.
	 */
	public DBLoadService() {
		setDatabase(DEFAULT_DB_PATH);
	}

	/**
	 * @param g	The database to use.
	 */
	public final void setDatabase(String g) {
		database.set(g);
	}

	/**
	 * @return The database to use, if any.
	 */
	public final String getDatabase() {
		return database.get();
	}

	/**
	 * @return The database property.
	 */
	public ObjectProperty<String> databaseProperty() {
		return database;
	}

	@Override
	protected Task<Graph> createTask() {
		return new Task<Graph>() {
			@Override
			protected Graph call() throws IOException, ParseException {
				return new Neo4jGraph(database.get());
			}
		};
	}
}
