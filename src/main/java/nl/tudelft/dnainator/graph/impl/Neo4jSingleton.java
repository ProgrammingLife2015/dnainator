package nl.tudelft.dnainator.graph.impl;

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
	private static final String DB_PATH = "target/dna-graph-db";

	private Map<String, Neo4jGraph> neodatabases;

	private Neo4jSingleton() {
		neodatabases = new HashMap<>();
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
	public Neo4jGraph getDatabase() {
		return getDatabase(DB_PATH);
	}

	/**
	 * Retrieves the database associated with a specified path, if it exists.
	 * Otherwise instantiates a new database.
	 * @param path	the path to the database
	 * @return		a database instance
	 */
	public Neo4jGraph getDatabase(String path) {
		if (!neodatabases.containsKey(path)) {
			neodatabases.put(path, new Neo4jGraph(path));
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
	 * Deletes the contents of the database associated with the default path, if it exists.
	 * FIXME: {@link Neo4jGraph} seems to be unable to restart on a previously shut down path.
	 */
	public void deleteDatabase() {
		deleteDatabase(DB_PATH);
	}

	/**
	 * Deletes the contents of the database associated with the specified path, if it exists.
	 * @param path	the path to the database
	 */
	public void deleteDatabase(String path) {
		if (neodatabases.containsKey(path)) {
			neodatabases.get(path).clear();
		}
	}

	/**
	 * Stops and deletes the the database associated with the specified path, if it exists.
	 * FIXME: {@link Neo4jGraph} seems to be unable to restart on a previously shut down path.
	 *        DO NOT USE THIS METHOD!
	 * @param path	the path to the database
	 * @throws IOException	when the database could not be deleted
	 */
	public void stopDatabase(String path) throws IOException {
		if (neodatabases.containsKey(path)) {
			neodatabases.get(path).shutdown();
			neodatabases.remove(path);
		}

		FileUtils.deleteRecursively(new File(path));
	}
}
