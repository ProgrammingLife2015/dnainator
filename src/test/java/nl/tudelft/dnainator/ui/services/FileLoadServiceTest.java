package nl.tudelft.dnainator.ui.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.concurrent.Service;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.Neo4jSingleton;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.io.fs.FileUtils;

import de.saxsys.javafx.test.JfxRunner;

/**
 * Test class for the {@link FileLoadService}.
 * <p>
 * See http://blog.buildpath.de/how-to-test-javafx-services/ for
 * an explanation on how to test JavaFX code.
 * </p>
 */
@RunWith(JfxRunner.class)
public class FileLoadServiceTest {
	private static final String DB_PATH = "target/neo4j-junit";
	private static final int DELAY = 20000;
	private FileLoadService loadService;
	private File nodeFile;
	private File edgeFile;

	static {
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
		loadService = new FileLoadService();
		try {
			nodeFile = new File(getClass().getResource("/strains/test.node.graph").toURI());
			edgeFile = new File(getClass().getResource("/strains/test.edge.graph").toURI());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}
		loadService.setNodeFile(nodeFile);
		loadService.setEdgeFile(edgeFile);
	}

	/**
	 * Tests setNodeFile().
	 */
	@Test
	public void testSetNodeFile() {
		try {
			File foo = File.createTempFile("foo", ".bar");
			loadService.setNodeFile(foo);
			assertEquals(foo, loadService.getNodeFile());
			foo.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests getNodeFile().
	 */
	@Test
	public void testGetNodeFile() {
		assertEquals(nodeFile, loadService.getNodeFile());
	}

	/**
	 * Tests nodeFileProperty().
	 */
	@Test
	public void testNodeFileProperty() {
		assertNotNull(loadService.nodeFileProperty());
	}

	/**
	 * Tests setEdgeFile().
	 */
	@Test
	public void testSetEdgeFile() {
		try {
			File foo = File.createTempFile("foo", ".bar");
			loadService.setEdgeFile(foo);
			assertEquals(foo, loadService.getEdgeFile());
			foo.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests getEdgeFile().
	 */
	@Test
	public void testGetEdgeFile() {
		assertEquals(edgeFile, loadService.getEdgeFile());
	}

	/**
	 * Tests edgeFileProperty().
	 */
	@Test
	public void testEdgeFileProperty() {
		assertNotNull(loadService.edgeFileProperty());
	}

	/**
	 * Registers a listener on the service to update the completableFuture.
	 * @param service The service.
	 * @param completableFuture	The future.
	 * @param <T> The expected type.
	 */
	public <T> void registerListeners(Service<T> service, CompletableFuture<T> completableFuture) {
		service.setOnSucceeded(e -> completableFuture.complete(service.getValue()));
		service.setOnFailed(e ->
			completableFuture.completeExceptionally(service.getException()));
		service.setOnCancelled(e -> completableFuture.cancel(true));
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
	}

	/**
	 * Tests loading a file properly.
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testProperFile() throws InterruptedException, ExecutionException, TimeoutException {
		doTest();
	}

	/**
	 * Tests loading a file that is not a node file.
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Test(expected = ExecutionException.class)
	public void notANodeFile() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			File wrongFile = new File(getClass().getResource("/strains/wrong.node.graph").toURI());
			loadService.setNodeFile(wrongFile);
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		doTest();
	}

	/**
	 * Tests loading a file that is not an edge file.
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Test(expected = ExecutionException.class)
	public void notAnEdgeFile() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			File wrongFile = new File(getClass().getResource("/strains/wrong.edge.graph").toURI());
			loadService.setEdgeFile(wrongFile);
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		doTest();
	}

	/**
	 * Clean up after ourselves.
	 * @throws IOException when the database could not be deleted
	 */
	@AfterClass
	public static void cleanUp() throws IOException {
		Neo4jSingleton.getInstance().stopDatabase(DB_PATH);
	}
}
