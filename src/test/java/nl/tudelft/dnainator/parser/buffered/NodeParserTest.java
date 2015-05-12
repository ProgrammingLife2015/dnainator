package nl.tudelft.dnainator.parser.buffered;

import static nl.tudelft.dnainator.parser.buffered.ParserTestUtils.toBufferedReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
import nl.tudelft.dnainator.core.impl.SequenceNodeImpl;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidHeaderFormatException;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;

import org.junit.Test;

/**
 * Tests the JFASTA implementation of the NodeParser interface.
 */
public class NodeParserTest {

	/**
	 * Tests empty input.
	 */
	@Test(expected = NoSuchElementException.class)
	public void testParseNodesEmpty() {
		BufferedReader in = toBufferedReader("");
		try {
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(), in);
			assertFalse(np.hasNext());
			np.next();
		} catch (IOException | InvalidHeaderFormatException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Test the equals method for the node id.
	 */
	@Test
	public void testNodeIdNotEquals() {
		SequenceNode a = new SequenceNodeImpl("a", "test", 1, 2, "ACTG");
		SequenceNode b = new SequenceNodeImpl("b", "test", 1, 2, "ACTG");

		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	/**
	 * Test the equals method for the node source.
	 */
	@Test
	public void testNodeSourceNotEquals() {
		SequenceNode a = new SequenceNodeImpl("a", "test", 1, 2, "ACTG");
		SequenceNode b = new SequenceNodeImpl("a", "tesd", 1, 2, "ACTG");

		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a, 0);
	}

	/**
	 * Test the equals method for the node startref.
	 */
	@Test
	public void testNodeStartNotEquals() {
		SequenceNode a = new SequenceNodeImpl("a", "test", 1, 2, "ACTG");
		SequenceNode b = new SequenceNodeImpl("a", "test", 2, 2, "ACTG");

		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	/**
	 * Test the equals method for the node endref.
	 */
	@Test
	public void testNodeEndNotEquals() {
		SequenceNode a = new SequenceNodeImpl("a", "test", 1, 2, "ACTG");
		SequenceNode b = new SequenceNodeImpl("a", "test", 1, 1, "ACTG");

		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	/**
	 * Test the equals method for the node sequence.
	 */
	@Test
	public void testNodeSequenceNotEquals() {
		SequenceNode a = new SequenceNodeImpl("a", "test", 1, 1, "ACTG");
		SequenceNode b = new SequenceNodeImpl("a", "test", 1, 2, "BCTG");

		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	/**
	 * Test the equals method for the node sequence.
	 */
	@Test
	public void testNodeEquals() {
		SequenceNode a = new SequenceNodeImpl("a", "test", 1, 2, "ACTG");

		assertEquals(a, a);
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a, new SequenceNodeImpl("a", "test", 1, 2, "ACTG"));
		assertEquals(a.hashCode(), new SequenceNodeImpl("a", "test", 1, 2, "ACTG").hashCode());
	}

	/**
	 * Test the toString method.
	 */
	@Test
	public void testToString() {
		SequenceNode a = new SequenceNodeImpl("a", "test", 1, 2, "ACTG");

		assertEquals("SequenceNode<a,test,1,2,ACTG>", a.toString());
	}

	/**
	 * Tests a good weather situation, where the input is of the correct format.
	 */
	@Test
	public void testParseNodesGood() {
		BufferedReader in = toBufferedReader(String.join("\n",
				"> 4 | ASDF | 0 | 42",
				"ACTGTGTGTATAT",
				"> 5 | FDSA | 42 | 84",
				"ATATATATATATA"
				));
		try {
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(), in);
			//CHECKSTYLE.OFF: MagicNumber
			assertTrue(np.hasNext());
			assertEquals(new SequenceNodeImpl("4", "ASDF", 0, 42, "ACTGTGTGTATAT"), np.next());
			assertTrue(np.hasNext());
			assertEquals(new SequenceNodeImpl("5", "FDSA", 42, 84, "ATATATATATATA"), np.next());
			assertFalse(np.hasNext());
			//CHECKSTYLE.ON: MagicNumber
		} catch (IOException | InvalidHeaderFormatException e) {
			fail("Shouldn't happen.");
		}
	}

	/**
	 * Tests spaces inserted in the input, which should be ignored.
	 */
	@Test
	public void testParseNodesSpaces() {
		BufferedReader in = toBufferedReader(String.join("\n",
				">    4 | ASDF    |  0 | 42",
				"    ACT GTGTGTATAT   AT",
				"> 5  |  FDSA   |  42  |   84    ",
				" A T A T A T A T A T A T A"
				));
		try {
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(), in);
			//CHECKSTYLE.OFF: MagicNumber
			assertTrue(np.hasNext());
			assertEquals(new SequenceNodeImpl("4", "ASDF", 0, 42, "ACTGTGTGTATATAT"), np.next());
			assertTrue(np.hasNext());
			assertEquals(new SequenceNodeImpl("5", "FDSA", 42, 84, "ATATATATATATA"), np.next());
			assertFalse(np.hasNext());
			//CHECKSTYLE.ON: MagicNumber
		} catch (IOException | InvalidHeaderFormatException e) {
			fail("Shouldn't happen.");
		}
	}

}
