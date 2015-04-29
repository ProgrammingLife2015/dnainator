package nl.tudelft.dnainator.parser.buffered;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.util.Edge;

import org.junit.Test;

public class DefaultEdgeParserTest {

	private static BufferedReader toBufferedReader(String s) {
		return new BufferedReader(new StringReader(s));
	}

	private static void assertEdgeEquals(Edge<Integer> expected, Edge<Integer> actual) {
		assertEquals(expected.source, actual.source);
		assertEquals(expected.dest, actual.dest);
	}

	@Test(expected = NoSuchElementException.class)
	public void testParseEmpty() {
		BufferedReader in = toBufferedReader("");
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertFalse(ep.hasNext());
			ep.next();
		} catch (IOException e) {
			fail("Shouldn't happen.");
		}

	}

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
			Edge<Integer> next = ep.next();
			assertEdgeEquals(new Edge<Integer>(1, 2), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<Integer>(3, 4), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<Integer>(6, 40), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<Integer>(123, 456), next);
			assertFalse(ep.hasNext());
		} catch (IOException e) {
			fail("Shouldn't happen.");
		}
	}

}