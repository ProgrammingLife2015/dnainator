package nl.tudelft.dnainator.ui.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static nl.tudelft.dnainator.ui.services.LoadServiceTestUtils.registerListeners;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;

/**
 * Test the loadservice for loading GFF3 files.
 */
@RunWith(JfxRunner.class)
public class GFFLoadServiceTest {
	private GFFLoadService loadService;
	private String gffFilePath;
	private static final int DELAY = 20000;

	/**
	 * Creates test node and edge files.
	 * @throws URISyntaxException 
	 */
	@Before
	public void setup() throws URISyntaxException {
		loadService = new GFFLoadService();
		gffFilePath = new File(getClass().getResource("/annotations/test.gff").toURI()).toString();
		loadService.setGffFilePath(gffFilePath);
	}

	/**
	 * Test loading of a proper file.
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 * @throws TimeoutException 
	 */
	@Test
	public void testProperFile() throws InterruptedException, ExecutionException, TimeoutException {
		doTest();
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
		CompletableFuture<AnnotationCollection> completableFuture = new CompletableFuture<>();

		// Act on the loadService's interesting states.
		registerListeners(loadService, completableFuture);

		loadService.getDatabase().set(GraphLoadServiceTest.DB_PATH);
		loadService.start();

		// This call blocks the test thread until the completableFuture's complete() method is
		// called. Calling get() retrieves the result (e.g., the Graph in this case). Set a
		// timeout in case the result does not appear.
		AnnotationCollection ac = completableFuture.get(DELAY, TimeUnit.MILLISECONDS);

		assertNotNull(ac);
		assertEquals(1, ac.getAll().size());
	}

	/**
	 * Clean up after ourselves.
	 * @throws IOException when the database could not be deleted
	 */
	@AfterClass
	public static void cleanUp() throws IOException {
		Neo4jSingleton.getInstance().stopDatabase(GraphLoadServiceTest.DB_PATH);
	}

}
