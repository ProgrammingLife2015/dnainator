package nl.tudelft.dnainator.parser.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.parser.Iterator;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the GFF annotation parser.
 */
public class GFF3AnnotationParserTest {
	/**
	 * Utility function that creates a parser.
	 * @param resourceFilePath The file path
	 * @return a parser
	 * @throws IOException when the file could not be opened
	 * @throws URISyntaxException when the uri is incorrect
	 */
	private Iterator<Annotation> createParser(String resourceFilePath) throws IOException,
		URISyntaxException {
		String gffFilePath = new File(getClass().getResource(resourceFilePath)
				.toURI()).toString();
		return new AnnotationIterator(gffFilePath);
	}

	/**
	 * Test ignoring annotations that are not of the "CDS" type.
	 * @throws IOException when the file could not be opened
	 * @throws URISyntaxException when the uri is incorrect
	 */
	@Test
	public void testIgnoreNonCDS() throws IOException, URISyntaxException {
		Iterator<Annotation> ap = createParser("/annotations/test.gff");
		assertEquals("TEST2", ap.next().getGeneName());
		assertEquals("TEST4", ap.next().getGeneName());
	}

	/**
	 * Test for {@link NoSuchElementException} when there's no more
	 * annotations of type "CDS".
	 * @throws IOException when the file could not be opened
	 * @throws URISyntaxException when the uri is incorrect
	 */
	@Test(expected = NoSuchElementException.class)
	public void testNoSuchElement() throws IOException, URISyntaxException {
		Iterator<Annotation> ap = createParser("/annotations/test.gff");
		assertEquals("TEST2", ap.next().getGeneName());
		assertEquals("TEST4", ap.next().getGeneName());
		System.out.println(ap.next());
	}

	/**
	 * Test the hasNext function.
	 * @throws IOException when the file could not be opened
	 * @throws URISyntaxException when the uri is incorrect
	 */
	@Test
	public void testHasNext() throws IOException, URISyntaxException {
		Iterator<Annotation> ap = createParser("/annotations/test.gff");
		assertTrue(ap.hasNext());
		ap.next();
		assertTrue(ap.hasNext());
		ap.next();
		assertFalse(ap.hasNext());
	}

	/**
	 * Test whether hasNext works when there's no elements from the start.
	 * @throws IOException when the file could not be opened
	 * @throws URISyntaxException when the uri is incorrect
	 */
	@Test
	public void testNotHasNext() throws IOException, URISyntaxException {
		Iterator<Annotation> ap = createParser("/annotations/test_no_cds.gff");
		assertFalse(ap.hasNext());
		assertFalse(ap.hasNext());
	}
}
