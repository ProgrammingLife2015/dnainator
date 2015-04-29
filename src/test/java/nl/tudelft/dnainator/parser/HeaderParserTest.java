package nl.tudelft.dnainator.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * JUnit test suite for {@link HeaderParser.java}.
 */
public class HeaderParserTest {

	/**
	 * Tests the correctness of the result of parsing.
	 */
	@Test
	public void testParseHeaderGood() {
		HeaderParser hp = new HeaderParser("1 | a,b,c | 3 | 5");
		try {
			assertEquals("1", hp.next());
			assertEquals("a,b,c", hp.next());
			assertEquals("3", hp.next());
			assertEquals("5", hp.next());
		} catch (InvalidHeaderFormatException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the parsing fails when a header contains too few elements.
	 */
	@Test
	public void testParseHeaderTooLittle() {
		HeaderParser hp = new HeaderParser("1 | a,b,c | 3");
		try {
			assertEquals("1", hp.next());
			assertEquals("a,b,c", hp.next());
			assertEquals("3", hp.next());
			assertEquals("5", hp.next());
		} catch (InvalidHeaderFormatException e) {
			assertEquals("Not enough tokens, wanted 4, got 3", e.getMessage());
		}
	}

	/**
	 * Tests the parsing fails when a header contains too many elements.
	 */
	@Test
	public void testParseHeaderTooMuch() {
		HeaderParser hp = new HeaderParser("1 | a,b,c | 3 | 5 | 6");
		try {
			assertEquals("1", hp.next());
			assertEquals("a,b,c", hp.next());
			assertEquals("3", hp.next());
			assertEquals("5", hp.next());
			assertEquals("6", hp.next());
		} catch (InvalidHeaderFormatException e) {
			assertEquals("Leftover string:  6", e.getMessage());
		}
	}
}
