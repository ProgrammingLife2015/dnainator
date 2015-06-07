package nl.tudelft.dnainator.ui.services;

import de.saxsys.javafx.test.JfxRunner;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jBatchBuilder;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;
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

import static nl.tudelft.dnainator.ui.services.LoadServiceTestUtils.registerListeners;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the loadservice for loading GFF3 files.
 */
@RunWith(JfxRunner.class)
public class GFFLoadServiceTest {
	private static final String DB_PATH = "target/neo4j-junit-gffload";
	private Graph graph;
	private GFFLoadService loadService;
	private String gffFilePath;
	private static final int DELAY = 20000;

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
	 * @throws URISyntaxException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Before
	public void setup() throws URISyntaxException, IOException, ParseException {
		File nodeFile = new File(getClass().getResource("/strains/test.node.graph").toURI());
		File edgeFile = new File(getClass().getResource("/strains/test.edge.graph").toURI());
		new Neo4jBatchBuilder(DB_PATH).constructGraph(new NodeParserImpl(nodeFile),
				new EdgeParserImpl(edgeFile));
		graph = new Neo4jGraph(DB_PATH);
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

		loadService.graphProperty().set(graph);
		loadService.start();

		// This call blocks the test thread until the completableFuture's complete() method is
		// called. Calling get() retrieves the result (e.g., the Graph in this case). Set a
		// timeout in case the result does not appear.
		AnnotationCollection ac = completableFuture.get(DELAY, TimeUnit.MILLISECONDS);

		assertNotNull(ac);
		assertEquals(2, ac.getAll().size());
	}

}
