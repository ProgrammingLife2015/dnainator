package nl.tudelft.dnainator.javafx.services;

import static nl.tudelft.dnainator.javafx.services.LoadServiceTestUtils.registerListeners;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.io.fs.FileUtils;

import de.saxsys.javafx.test.JfxRunner;

/**
 * Test class for the {@link NewickLoadService}.
 * <p>
 * See http://blog.buildpath.de/how-to-test-javafx-services/ for
 * an explanation on how to test JavaFX code.
 * </p>
 */
@RunWith(JfxRunner.class)
public class DBLoadServiceTest {
	private DBLoadService loadService;
	private static final int DELAY = 20000;
	private static final String DEFAULT_DB_PATH = "target" + File.separator + "db";
	private static final String DB_PATH = "target/neo4j-junit-dbload";
	
	/**
	 * Delete old database.
	 */
	@BeforeClass
	public static void setUp() {
		try {
			FileUtils.deleteRecursively(new File(DB_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates new database service.
	 */
	@Before
	public void setup() {
		loadService = new DBLoadService();
	}
	
	/**
	 * Test the default database path and changing the path.
	 */
	@Test
	public void testDBPath() {
		assertEquals(loadService.getDatabase(), DEFAULT_DB_PATH);
		
		loadService.setDatabase("other path");
		assertEquals(loadService.getDatabase(), "other path");
	}
	
	/**
	 * Test the database property.
	 */
	@Test
	public void testDatabaseProperty() {
		assertNotNull(loadService.databaseProperty());
	}
	
	/**
	 * Performs the actual test, in that it initializes the CompletableFuture,
	 * attaches the listeners and does the assertions.
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 * @throws TimeoutException 
	 */
	public void doTest() throws InterruptedException, ExecutionException, TimeoutException {
		// Create a completableFuture to test the background task of the service. This
		// completableFuture blocks the test thread until its complete method was called.
		CompletableFuture<Graph> completableFuture = new CompletableFuture<>();

		// Act on the loadService's interesting states.
		registerListeners(loadService, completableFuture);

		loadService.setDatabase(DB_PATH);
		loadService.start();

		// This call blocks the test thread until the completableFuture's complete() method is
		// called. Calling get() retrieves the result (e.g., the Graph in this case). Set a
		// timeout in case the result does not appear.
		Graph graph = completableFuture.get(DELAY, TimeUnit.MILLISECONDS);

		assertNotNull(graph);
		((Neo4jGraph) graph).shutdown();
	}
	
	/**
	 * Tests loading a database properly.
	 * @throws TimeoutException Thrown on timeout.
	 * @throws ExecutionException Thrown when an exception occurs when trying to execute.
	 * @throws InterruptedException Thrown when the service is interrupted.
	 */
	@Test
	public void testProperDatabase() throws InterruptedException, 
		ExecutionException, TimeoutException {
		doTest();
	}

}
