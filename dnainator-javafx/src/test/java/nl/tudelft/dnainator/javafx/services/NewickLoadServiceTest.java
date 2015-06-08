package nl.tudelft.dnainator.javafx.services;

import de.saxsys.javafx.test.JfxRunner;
import nl.tudelft.dnainator.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
 * Test class for the {@link NewickLoadService}.
 * <p>
 * See http://blog.buildpath.de/how-to-test-javafx-services/ for
 * an explanation on how to test JavaFX code.
 * </p>
 */
@RunWith(JfxRunner.class)
public class NewickLoadServiceTest {
	private static final int DELAY = 20000;
	private NewickLoadService loadService;
	private File newickFile;

	/**
	 * Creates test newick file.
	 */
	@Before
	public void setup() {
		loadService = new NewickLoadService();
		try {
			newickFile = new File(getClass().getResource("/strains/test.nwk").toURI());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}
		loadService.setNewickFile(newickFile);
	}

	/**
	 * Tests setNewickFile().
	 */
	@Test
	public void testSetNewickFile() {
		try {
			File foo = File.createTempFile("foo", ".bar");
			loadService.setNewickFile(foo);
			assertEquals(foo, loadService.getNewickFile());
			foo.delete();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests getNewickFile().
	 */
	@Test
	public void testGetNewickFile() {
		assertEquals(newickFile, loadService.getNewickFile());
	}

	/**
	 * Tests newickFileProperty().
	 */
	@Test
	public void testNewickFileProperty() {
		assertNotNull(loadService.newickFileProperty());
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
		CompletableFuture<TreeNode> completableFuture = new CompletableFuture<>();

		// Act on the loadService's interesting states.
		registerListeners(loadService, completableFuture);

		loadService.start();

		// This call blocks the test thread until the completableFuture's complete() method is
		// called. Calling get() retrieves the result (e.g., the TreeNode in this case). Set a
		// timeout in case the result does not appear.
		TreeNode treeNode = completableFuture.get(DELAY, TimeUnit.MILLISECONDS);

		assertNotNull(treeNode);
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
	 * Tests loading a file that is not a newick file.
	 * @throws TimeoutException Thrown on timeout.
	 * @throws ExecutionException Thrown when an exception occurs when trying to execute.
	 * @throws InterruptedException Thrown when the service is interrupted.
	 */
	@Test(expected = ExecutionException.class)
	public void notANewickFile() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			File wrongFile = new File(getClass().getResource("/strains/wrong.nwk").toURI());
			loadService.setNewickFile(wrongFile);
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		doTest();
	}
}