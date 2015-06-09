package nl.tudelft.dnainator.javafx.services;

import de.saxsys.javafx.test.JfxRunner;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static nl.tudelft.dnainator.javafx.services.LoadServiceTestUtils.registerListeners;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Test class for the {@link GraphLoadService}.
 * <p>
 * See http://blog.buildpath.de/how-to-test-javafx-services/ for
 * an explanation on how to test JavaFX code.
 * </p>
 */
@RunWith(JfxRunner.class)
public class GraphLoadServiceTest {
	private static final String DB_PATH = "target/neo4j-junit-graphload";
	private static final int DELAY = 20000;
	private GraphLoadService loadService;
	private String nodePath;
	private String edgePath;

	/**
	 * Setup the database and construct the graph.
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
	 * Creates test node and edge files.
	 */
	@Before
	public void setup() {
		loadService = new GraphLoadService();
		nodePath = getClass().getResource("/strains/test.node.graph").getFile();
		edgePath = getClass().getResource("/strains/test.edge.graph").getFile();
		loadService.setNodePath(nodePath);
		loadService.setEdgePath(edgePath);
	}

	/**
	 * Tests setNodePath().
	 */
	@Test
	public void testSetNodeFile() {
		String foo = "foo.bar";
		loadService.setNodePath(foo);
		assertEquals(foo, loadService.getNodePath());
	}

	/**
	 * Tests getNodePath().
	 */
	@Test
	public void testGetNodeFile() {
		assertEquals(nodePath, loadService.getNodePath());
	}

	/**
	 * Tests nodePathProperty().
	 */
	@Test
	public void testNodeFileProperty() {
		assertNotNull(loadService.nodePathProperty());
	}

	/**
	 * Tests setEdgePath().
	 */
	@Test
	public void testSetEdgeFile() {
		String foo = "foo.bar";
		loadService.setEdgePath(foo);
		assertEquals(foo, loadService.getEdgePath());
	}

	/**
	 * Tests getEdgePath().
	 */
	@Test
	public void testGetEdgeFile() {
		assertEquals(edgePath, loadService.getEdgePath());
	}

	/**
	 * Tests edgePathProperty().
	 */
	@Test
	public void testEdgeFileProperty() {
		assertNotNull(loadService.edgePathProperty());
	}

	/**
	 * Performs the actual test, in that it initializes the CompletableFuture,
	 * attaches the listeners and does the assertions.
	 * @throws TimeoutException Thrown on timeout.
	 * @throws ExecutionException Thrown when an exception occurs when trying to execute.
	 * @throws InterruptedException Thrown when the service is interrupted.
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
	 * Tests loading a file properly.
	 * @throws TimeoutException Thrown on timeout.
	 * @throws ExecutionException Thrown when an exception occurs when trying to execute.
	 * @throws InterruptedException Thrown when the service is interrupted.
	 */
	@Test
	public void testProperFile() throws InterruptedException, ExecutionException, TimeoutException {
		doTest();
	}

	/**
	 * Tests loading a file that is not a node file.
	 * @throws TimeoutException Thrown on timeout.
	 * @throws ExecutionException Thrown when an exception occurs when trying to execute.
	 * @throws InterruptedException Thrown when the service is interrupted.
	 */
	@Test(expected = ExecutionException.class)
	public void notANodeFile() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			File wrongFile = new File(getClass().getResource("/strains/wrong.node.graph").toURI());
			loadService.setNodePath(wrongFile.getPath());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		doTest();
	}

	/**
	 * Tests loading a file that is not an edge file.
	 * @throws TimeoutException Thrown on timeout.
	 * @throws ExecutionException Thrown when an exception occurs when trying to execute.
	 * @throws InterruptedException Thrown when the service is interrupted.
	 */
	@Test(expected = ExecutionException.class)
	public void notAnEdgeFile() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			File wrongFile = new File(getClass().getResource("/strains/wrong.edge.graph").toURI());
			loadService.setEdgePath(wrongFile.getPath());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		doTest();
	}
}
