package nl.tudelft.dnainator.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests the {@link Range} used for annotations.
 */
public class RangeTest {

	/**
	 * Tests if the Range is created properly.
	 */
	@Test
	public void testCreation() {
		Range r = new Range(0, 1);
		assertEquals(0, r.getX());
		assertEquals(1, r.getY());
	}

	/**
	 * Tests if the returned range has an inclusive start coordinate and an exclusive end
	 * coordinate.
	 */
	@Test
	public void testGetExclusiveEnd() {
		Range r = new Range(0, 1);
		Range exclusiveEndRange = new Range(0, 0);
		assertEquals(exclusiveEndRange, r.getExclusiveEnd());
	}

	/**
	 * Tests if comparing ranges yields the correct outcome.
	 */
	@Test
	public void testCompareTo() {
		//CHECKSTYLE.OFF: MagicNumber
		Range normal = new Range(3, 5);
		Range larger = new Range(6, 10);
		Range smaller = new Range(1, 2);
		Range equal = new Range(3, 5);
		Range overlap = new Range(1, 6);
		//CHECKSTYLE.ON: MagicNumber

		assertEquals(1, normal.compareTo(smaller));
		assertEquals(-1, normal.compareTo(larger));
		assertEquals(0, normal.compareTo(equal));
		assertEquals(0, normal.compareTo(overlap));
	}

	/**
	 * Tests if equals() and hashCode() work correctly.
	 */
	@Test
	public void testEqualsAndHashCode() {
		//CHECKSTYLE.OFF: MagicNumber
		Range r = new Range(0, 1);
		Range r2 = new Range(4, 5);
		//CHECKSTYLE.ON: MagicNumber
		assertFalse(r.equals(null));
		assertFalse(r.equals(r2));
		assertFalse(r.equals(new Object()));
		assertEquals(r.hashCode(), r2.hashCode());
	}

	/**
	 * Test toString().
	 */
	@Test
	public void testToString() {
		assertEquals("from: 0, to: 1", new Range(0, 1).toString());
	}
}
