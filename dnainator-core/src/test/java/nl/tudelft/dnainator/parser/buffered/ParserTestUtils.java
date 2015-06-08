package nl.tudelft.dnainator.parser.buffered;

import nl.tudelft.dnainator.core.impl.Edge;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * Utility functions for parser tests.
 */
final class ParserTestUtils {

	private ParserTestUtils() {
		
	}

	/**
	 * Convert a String to a BufferedReader.
	 * @param s The string to be parsed.
	 * @return a BufferedReader which delivers the characters.
	 */
	protected static BufferedReader toBufferedReader(String s) {
		return new BufferedReader(new StringReader(s));
	}

	/**
	 * Utility method for testing equality of two edges.
	 * @param expected the expected Edge.
	 * @param actual the actual Edge.
	 */
	protected static void assertEdgeEquals(Edge<String> expected, Edge<String> actual) {
		assertEquals(expected.getSource(), actual.getSource());
		assertEquals(expected.getDest(), actual.getDest());
	}
}
