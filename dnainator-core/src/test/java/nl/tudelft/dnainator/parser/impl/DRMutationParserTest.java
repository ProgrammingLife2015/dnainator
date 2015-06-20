package nl.tudelft.dnainator.parser.impl;

import nl.tudelft.dnainator.annotation.impl.DRMutation;
import nl.tudelft.dnainator.parser.Parser;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link DRMutationParserImpl}.
 */
public class DRMutationParserTest {
	private Parser<DRMutation> ap;

	private Parser<DRMutation> createParser(String resourceFilePath) throws IOException,
			URISyntaxException {
		File drFile = new File(getClass().getResource(resourceFilePath).toURI());
		return new DRMutationParserImpl(drFile);
	}

	/**
	 * Cleanly dispose of the parser.
	 * @throws IOException if closing fails.
	 */
	@After
	public void close() throws IOException {
		ap.close();
	}

	/**
	 * Test ignoring comments.
	 * @throws IOException when reading fails.
	 * @throws URISyntaxException when creating the parser fails.
	 */
	@Test
	public void testIgnoreComments() throws IOException, URISyntaxException {
		ap = createParser("/annotations/dr_test.txt");
		// Parse the mutation.
		ap.next();
		// The file contains 3 lines, but the first two are comments.
		assertFalse(ap.hasNext());
	}

	/**
	 * Test for {@link NoSuchElementException} when there are no more mutations.
	 * @throws IOException when reading fails.
	 * @throws URISyntaxException when creating the parser fails.
	 */
	@Test(expected = NoSuchElementException.class)
	public void testNoSuchElement() throws IOException, URISyntaxException {
		ap = createParser("/annotations/dr_test.txt");
		ap.next();
		// No more mutations left.
		ap.next();
	}

	/**
	 * Test the hasNext function.
	 * @throws IOException when reading fails.
	 * @throws URISyntaxException when creating the parser fails.
	 */
	@Test
	public void testHasNext() throws IOException, URISyntaxException {
		ap = createParser("/annotations/dr_test.txt");
		assertTrue(ap.hasNext());
		ap.next();
		assertFalse(ap.hasNext());
	}

	/**
	 * Test whether hasNext works when there's no elements from the start.
	 * @throws IOException when reading fails.
	 * @throws URISyntaxException when creating the parser fails.
	 */
	@Test
	public void testNotHasNext() throws IOException, URISyntaxException {
		ap = createParser("/annotations/dr_wrong.txt");
		assertFalse(ap.hasNext());
	}
}
