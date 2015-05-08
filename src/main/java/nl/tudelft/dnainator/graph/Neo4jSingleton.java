package nl.tudelft.dnainator.graph;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.neo4j.io.fs.FileUtils;

/**
 * This singleton class instantiates and keeps track of all Neo4j databases.
 */
public final class Neo4jSingleton {
	private static final Neo4jSingleton INSTANCE = new Neo4jSingleton();
	public static final String DB_PATH = "target/neo4j-hello-db";

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
	 */
	public Neo4jGraphDatabase getDatabase() {
		return getDatabase(DB_PATH);
	}

	/**
	 * Retrieves the database associated with a specified path, if it exists.
	 * Otherwise instantiates a new database.
	 * @param path	the path to the database
	 * @return		a database instance
	 */
	public Neo4jGraphDatabase getDatabase(String path) {
		if (!neodatabases.containsKey(path)) {
			neodatabases.put(path, new Neo4jGraphDatabase(path));
		}

		return neodatabases.get(path);
	}

	/**
	 * Returns the database paths of all databases instantiated through this singleton.
	 * @return		a set of all database paths
	 */
	public Set<String> getDatabasePaths() {
		return neodatabases.keySet();
	}

	/**
	 * Stops the database associated with the specified path, if it exists.
	 * FIXME: {@link Neo4jGraph} does not handle persistence very well yet
	 * @param path	the path to the database
	 * @throws IOException	when the database could not be deleted 
	 */
	public void stopDatabase(String path) throws IOException {
		if (neodatabases.containsKey(path)) {
			neodatabases.get(path).getService().shutdown();
			neodatabases.remove(path);
		}
		FileUtils.deleteRecursively(new File(path));
	}

	// FIXME: {@link Neo4jGraph} does not handle persistence very well yet
	//	/**
	//	 * Stops and deletes the database associated with the specified path, if it exists.
	//	 * @param path	the path to the database
	//	 * @throws IOException	when the database could not be deleted
	//	 */
	//	public void deleteDatabase(String path) throws IOException {
	//		stopDatabase(path);
	//		FileUtils.deleteRecursively(new File(path));
	//	}
}
