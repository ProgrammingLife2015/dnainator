package nl.tudelft.dnainator.graph;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.io.fs.FileUtils;

/**
 * This singleton class instantiates and keeps track of all Neo4j databases.
 */
public final class Neo4jSingleton {
	private static final Neo4jSingleton INSTANCE = new Neo4jSingleton();
	private static final String DB_PATH = "target/neo4j-hello-db";

	private Map<String, Neo4jGraphDatabase> neodatabases;

	private Neo4jSingleton() {
		neodatabases = new HashMap<String, Neo4jGraphDatabase>();
	}

	/**
	 * Returns the unique instance of the Neo4jSingleton.
	 * @return	the unique Neo4jSingleton
	 */
	public static Neo4jSingleton getInstance() {
		return INSTANCE;
	}

	/**
	 * Retrieves the database associated with the default path, if it exists.
	 * Otherwise instantiates a new database.
	 * @return		a database instance
	 * @throws IOException	when Neo4j could not be started
	 */
	public Neo4jGraphDatabase getDatabase() throws IOException {
		return getDatabase(DB_PATH);
	}

	/**
	 * Retrieves the database associated with a specified path, if it exists.
	 * Otherwise instantiates a new database.
	 * @param path	the path to the database
	 * @return		a database instance
	 * @throws IOException	when Neo4j could not be started
	 */
	public Neo4jGraphDatabase getDatabase(String path) throws IOException {
		if (!neodatabases.containsKey(path)) {
			FileUtils.deleteRecursively(new File(path));
			neodatabases.put(path, new Neo4jGraphDatabase(path));
		}

		return neodatabases.get(path);
	}

	/**
	 * Stops the database associated with the specified path, if it exists.
	 * @param path	the path to the database
	 */
	public void stopDatabase(String path) {
		if (neodatabases.containsKey(path)) {
			// Database will be killed by the garbage collector eventually
			// Might consider implementing a possibility to force kill a database.
			neodatabases.remove(path);
		}
	}
}
