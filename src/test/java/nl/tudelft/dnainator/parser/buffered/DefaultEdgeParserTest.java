package nl.tudelft.dnainator.parser.buffered;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;
import static nl.tudelft.dnainator.parser.buffered.ParserTestUtils.toBufferedReader;
import static nl.tudelft.dnainator.parser.buffered.ParserTestUtils.assertEdgeEquals;

import org.junit.Test;

/**
 * Test the {@link DefaultEdgeParser} implementation of an
 * {@link EdgeParser}.
 */
public class DefaultEdgeParserTest {

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

	/**
	 * Tests parsing lines containing leading, middle and trailing
	 * whitespace. The parser should ignore this.
	 */
	@Test
	public void testParseEdgesLeadingMiddleAndTrailingSpaces() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"  1 2",			// Leading spaces.
				"3    4",			// Middle spaces.
				"6 40   ",			// Trailing spaces.
				"  123   456   "	// All of them at once.
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

	/**
	 * Tests exception throwing.
	 * @throws InvalidEdgeFormatException on test success.
	 */
	@Test(expected = InvalidEdgeFormatException.class)
	public void testParseEdgesMissingDest() throws InvalidEdgeFormatException {
		BufferedReader in = toBufferedReader(String.join("\n",
				"1 2",
				"3 ",
				"4 5"
				));
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertTrue(ep.hasNext());
			Edge<String> next = ep.next();
			assertEdgeEquals(new Edge<>("1", "2"), next);
			ep.next();
		} catch (IOException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Tests exception throwing.
	 * @throws InvalidEdgeFormatException on test success.
	 */
	@Test(expected = InvalidEdgeFormatException.class)
	public void testParseEdgesExtraNode() throws InvalidEdgeFormatException {
		BufferedReader in = toBufferedReader(String.join("\n",
				"1 2",
				"3 4 5",
				"6 7"
				));
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertTrue(ep.hasNext());
			Edge<String> next = ep.next();
			assertEdgeEquals(new Edge<>("1", "2"), next);
			ep.next();
		} catch (IOException e) {
			fail("Shouldn't happen.");
		}
	}


	/**
	 * Test for a empty line.
	 */
	@Test
	public void testParseEdgesEmptyLine() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"1 2",
				""
				));
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertTrue(ep.hasNext());
			Edge<String> next = ep.next();
			assertEdgeEquals(new Edge<>("1", "2"), next);
			assertFalse(ep.hasNext());
		} catch (IOException | InvalidEdgeFormatException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Test for a space-filled line.
	 */
	@Test
	public void testParseEdgesBlankLine() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"1 2",
				"    "
				));
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertTrue(ep.hasNext());
			Edge<String> next = ep.next();
			assertEdgeEquals(new Edge<>("1", "2"), next);
			assertFalse(ep.hasNext());
		} catch (IOException | InvalidEdgeFormatException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Test for a space-filled line in the middle.
	 */
	@Test
	public void testParseEdgesBlankLineInMiddle() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"1 2",
				"    ",
				"3 4"
				));
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertTrue(ep.hasNext());
			Edge<String> next = ep.next();
			assertEdgeEquals(new Edge<>("1", "2"), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<>("3", "4"), next);
		} catch (IOException | InvalidEdgeFormatException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Test for a empty line in the middle.
	 */
	@Test
	public void testParseEdgesEmptyLineInMiddle() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"1 2",
				"",
				"3 4"
				));
		EdgeParser ep = new DefaultEdgeParser(in);
		try {
			assertTrue(ep.hasNext());
			Edge<String> next = ep.next();
			assertEdgeEquals(new Edge<>("1", "2"), next);
			assertTrue(ep.hasNext());
			next = ep.next();
			assertEdgeEquals(new Edge<>("3", "4"), next);
		} catch (IOException | InvalidEdgeFormatException e) {
			fail("Shouldn't happen.");
		}
	}
}
