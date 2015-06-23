package nl.tudelft.dnainator.javafx;

import nl.tudelft.dnainator.javafx.exceptions.AllColorsInUseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testset for the {@link ColorMap}.
 */
public class ColorMapTest {
	private static final String TKK_REF = "TKK_REF";
	private static final String TKK_REF_0026 = "TKK_REF_0026";
	private ColorMap colorMap;

	/**
	 * Sets up the test environment.
	 */
	@Before
	public void setup() {
		colorMap = new ColorMap();
	}

	/**
	 * Tests whether simply getting one color that is not used yet, works.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorSimple() throws AllColorsInUseException {
		colorMap.askColor(TKK_REF);
		String col0 = colorMap.getColor(TKK_REF);
		assertEquals("color-0", col0);
		colorMap.revokeColor(TKK_REF);
	}

	/**
	 * Tests if the {@link ColorMap} correctly increments the count.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorMultiple() throws AllColorsInUseException {
		colorMap.askColor(TKK_REF);
		String col0 = colorMap.getColor(TKK_REF);
		assertEquals("color-0", col0);
		colorMap.askColor(TKK_REF_0026);
		String col1 = colorMap.getColor(TKK_REF_0026);
		assertEquals("color-1", col1);
		colorMap.revokeColor(TKK_REF);
		colorMap.revokeColor(TKK_REF_0026);
	}

	/**
	 * Tests if retrieving a color for the same strain retrieves the same {@link String}.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorSame() throws AllColorsInUseException {
		colorMap.askColor(TKK_REF);
		String col0 = colorMap.getColor(TKK_REF);
		String col1 = colorMap.getColor(TKK_REF);
		assertTrue(col0 == col1);
		colorMap.revokeColor(TKK_REF);
	}

	/**
	 * Tests if revoking a color works properly.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testRevoke() throws AllColorsInUseException {
		colorMap.askColor(TKK_REF);
		String col0 = colorMap.getColor(TKK_REF);
		assertEquals("color-0", col0);
		colorMap.revokeColor(TKK_REF);
		colorMap.askColor(TKK_REF);
		String col1 = colorMap.getColor(TKK_REF);
		assertEquals("color-0", col1);
		assertTrue(col0 == col1);
		colorMap.revokeColor(TKK_REF);
	}
	
	/**
	 * Test if an exception is thrown when all colors are taken.
	 * @throws AllColorsInUseException when all colors are in use.
	 */
	@Test(expected = AllColorsInUseException.class)
	public void testException() throws AllColorsInUseException {
		// CHECKSTYLE.OFF: MagicNumber
		for (int i = 0; i < 22; i++) {
			colorMap.askColor(TKK_REF + i);
		}
		// CHECKSTYLE.ON: MagicNumber
	}
}
