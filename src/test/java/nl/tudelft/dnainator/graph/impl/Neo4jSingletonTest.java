package nl.tudelft.dnainator.graph.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import nl.tudelft.dnainator.graph.Neo4jGraphDatabase;
import nl.tudelft.dnainator.graph.Neo4jSingleton;

import org.junit.Test;
import org.neo4j.io.fs.FileUtils;

/**
 * Test class for the Neo4jSingleton.
 */
public class Neo4jSingletonTest {
	private static final String DB1 = "target/neo4j-test-db-1";
	private static final String DB2 = "target/neo4j-test-db-2";

	static {
		try {
			FileUtils.deleteRecursively(new File(DB1));
			FileUtils.deleteRecursively(new File(DB2));

			// Start all database before the tests to prevent dependency on ordering
			Neo4jSingleton.getInstance().getDatabase(DB1);
			Neo4jSingleton.getInstance().getDatabase(DB2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verify that only one instance of the {@link Neo4jSingleton} can be active at a time.
	 */
	@Test 
	public void testUnique() {
		assertTrue(Neo4jSingleton.getInstance() == Neo4jSingleton.getInstance());
	}

	/**
	 * Verify that a database is unique and that all returned databases for a path are the same.
	 */
	@Test
	public void testUniqueCreation() {
		Neo4jGraphDatabase g1 = Neo4jSingleton.getInstance().getDatabase(DB1);
		Neo4jGraphDatabase g2 = Neo4jSingleton.getInstance().getDatabase(DB1);
		assertTrue(g1 == g2);
		assertEquals(2, Neo4jSingleton.getInstance().getDatabasePaths().size());
	}

	/**
	 * Verify that all databases associated with a certain path are
	 * unique and that all returned databases for one certain path are the same.
	 */
	@Test
	public void testUniqueCreationMultiple() {
		Neo4jGraphDatabase g1 = Neo4jSingleton.getInstance().getDatabase(DB1);
		Neo4jGraphDatabase g2 = Neo4jSingleton.getInstance().getDatabase(DB2);
		assertFalse(g1 == g2);
		assertEquals(2, Neo4jSingleton.getInstance().getDatabasePaths().size());

		Neo4jGraphDatabase g3 = Neo4jSingleton.getInstance().getDatabase(DB1);
		Neo4jGraphDatabase g4 = Neo4jSingleton.getInstance().getDatabase(DB2);
		assertTrue(g1 == g3);
		assertTrue(g2 == g4);
		assertFalse(g3 == g4);
		assertEquals(2, Neo4jSingleton.getInstance().getDatabasePaths().size());
	}

	/**
	 * Verify that a database associated with a certain path can be stopped and deleted.
	 * When a unique instance of a database is stopped and then started on the same path,
	 * the returned instance will be a NEW unique database on this path.
	 * @throws IOException	when the database could not be deleted
	 */
	@Test
	public void testStop() throws IOException {
		Neo4jGraphDatabase g1 = Neo4jSingleton.getInstance().getDatabase(DB1);
		Neo4jSingleton.getInstance().stopDatabase(DB1);
		Neo4jGraphDatabase g2 = Neo4jSingleton.getInstance().getDatabase(DB1);
		assertFalse(g1 == g2);
	}

	/**
	 * Verify that the state of the singleton does not change
	 * when a non-existent database is stopped.
	 * @throws IOException	when the database could not be deleted
	 */
	@Test
	public void testNonExistent() throws IOException {
		assertEquals(2, Neo4jSingleton.getInstance().getDatabasePaths().size());
		Neo4jSingleton.getInstance().stopDatabase("non-existent");
		assertEquals(2, Neo4jSingleton.getInstance().getDatabasePaths().size());
	}
}
