package nl.tudelft.dnainator.parser.buffered;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;

import org.junit.Test;

/**
 * Test the {@link DefaultEdgeParser} implementation of an
 * {@link EdgeParser}.
 */
public class DefaultEdgeParserTest {

	private static BufferedReader toBufferedReader(String s) {
		return new BufferedReader(new StringReader(s));
	}

	private static void assertEdgeEquals(Edge<String> expected, Edge<String> actual) {
		assertEquals(expected.getSource(), actual.getSource());
		assertEquals(expected.getDest(), actual.getDest());
	}

	/**
	 * Tests an empty input.
	 */
	@Test(expected = NoSuchElementException.class)
	public void testParseEmpty() {
		BufferedReader in = toBufferedReader("");
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertFalse(ep.hasNext());
			ep.next();
		} catch (IOException | InvalidEdgeFormatException e) {
			fail("Shouldn't happen.");
		}

	}

	/**
	 * Tests a good weather situation, where the input is in
	 * correct format.
	 */
	@Test
	public void testParseEdgesGood() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"1 2",
				"3 4",
				"6 40",
				"123 456"
				));
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertTrue(ep.hasNext());
			Edge<String> next = ep.next();
			assertEdgeEquals(new Edge<>("1", "2"), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<>("3", "4"), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<>("6", "40"), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<>("123", "456"), next);
			assertFalse(ep.hasNext());
		} catch (IOException | InvalidEdgeFormatException e) {
			fail("Shouldn't happen.");
		}
	}

}
