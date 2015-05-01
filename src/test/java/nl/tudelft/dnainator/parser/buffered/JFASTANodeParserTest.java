package nl.tudelft.dnainator.parser.buffered;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static nl.tudelft.dnainator.parser.buffered.ParserTestUtils.toBufferedReader;
import static nl.tudelft.dnainator.parser.buffered.ParserTestUtils.assertNodeEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import nl.tudelft.dnainator.core.DefaultSequenceFactory;
import nl.tudelft.dnainator.core.DefaultSequenceNode;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidHeaderFormatException;

import org.junit.Test;

/**
 * Tests the JFASTA implementation of the NodeParser interface.
 */
public class JFASTANodeParserTest {

	/**
	 * Tests empty input.
	 */
	@Test(expected = NoSuchElementException.class)
	public void testParseNodesEmpty() {
		BufferedReader in = toBufferedReader("");
		try {
			NodeParser np = new JFASTANodeParser(new DefaultSequenceFactory(), in);
			assertFalse(np.hasNext());
			np.next();
		} catch (IOException | InvalidHeaderFormatException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Tests a good weather situation, where the input is of the
	 * correct format.
	 */
	@Test
	public void testParseNodesGood() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"> 4 | ASDF | 0 | 42",
				"ACTGTGTGTATATATAT",
				"> 5 | FDSA | 42 | 84",
				"ATATATATATATATATA"
				));
		try {
			NodeParser np = new JFASTANodeParser(new DefaultSequenceFactory(), in);
			//CHECKSTYLE.OFF: MagicNumber
			assertTrue(np.hasNext());
			assertNodeEquals(
					new DefaultSequenceNode("4", "ASDF", 0, 42, "ACTGTGTGTATATATAT"), np.next());
			assertTrue(np.hasNext());
			assertNodeEquals(
					new DefaultSequenceNode("5", "FDSA", 42, 84, "ATATATATATATATATA"), np.next());
			assertFalse(np.hasNext());
			//CHECKSTYLE.ON: MagicNumber
		} catch (IOException | InvalidHeaderFormatException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Tests spaces inserted in the input, which should be
	 * ignored.
	 */
	@Test
	public void testParseNodesSpaces() {
		BufferedReader in = toBufferedReader(String.join("\n",
				">    4 | ASDF    |  0 | 42",
				"    ACT GTGTGTATAT   ATAT",
				"> 5  |  FDSA   |  42  |   84    ",
				" A T A T A T A T A T A T A T A T A"
				));
		try {
			NodeParser np = new JFASTANodeParser(new DefaultSequenceFactory(), in);
			//CHECKSTYLE.OFF: MagicNumber
			assertTrue(np.hasNext());
			assertNodeEquals(
					new DefaultSequenceNode("4", "ASDF", 0, 42, "ACTGTGTGTATATATAT"), np.next());
			assertTrue(np.hasNext());
			assertNodeEquals(
					new DefaultSequenceNode("5", "FDSA", 42, 84, "ATATATATATATATATA"), np.next());
			assertFalse(np.hasNext());
			//CHECKSTYLE.ON: MagicNumber
		} catch (IOException | InvalidHeaderFormatException e) {
			fail("Shouldn't happen.");
		}
	}

}
