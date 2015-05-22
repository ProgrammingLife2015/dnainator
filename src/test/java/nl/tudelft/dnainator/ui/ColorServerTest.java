package nl.tudelft.dnainator.ui;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testset for the {@link ColorServer}.
 */
public class ColorServerTest {
	private static final String TKK_REF = "TKK_REF";
	private static final String TKK_REF_0026 = "TKK_REF_0026";

	/**
	 * Tests whether simply getting one color that is not used yet, works.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorSimple() throws AllColorsInUseException {
		String col0 = ColorServer.getInstance().getColor(TKK_REF);
		assertEquals("color-0", col0);
		ColorServer.getInstance().revokeColor(TKK_REF);
	}

	/**
	 * Tests if the {@link ColorServer} correctly increments the count.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorMultiple() throws AllColorsInUseException {
		String col0 = ColorServer.getInstance().getColor(TKK_REF);
		assertEquals("color-0", col0);
		String col1 = ColorServer.getInstance().getColor(TKK_REF_0026);
		assertEquals("color-1", col1);
		ColorServer.getInstance().revokeColor(TKK_REF);
		ColorServer.getInstance().revokeColor(TKK_REF_0026);
	}

	/**
	 * Tests if retrieving a color for the same strain retrieves the same {@link String}.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorSame() throws AllColorsInUseException {
		String col0 = ColorServer.getInstance().getColor(TKK_REF);
		String col1 = ColorServer.getInstance().getColor(TKK_REF);
		assertTrue(col0 == col1);
		ColorServer.getInstance().revokeColor(TKK_REF);
	}

	/**
	 * Tests if revoking a color works properly.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testRevoke() throws AllColorsInUseException {
		String col0 = ColorServer.getInstance().getColor(TKK_REF);
		assertEquals("color-0", col0);
		ColorServer.getInstance().revokeColor(TKK_REF);
		String col1 = ColorServer.getInstance().getColor(TKK_REF);
		assertEquals("color-0", col1);
		assertFalse(col0 == col1);
		ColorServer.getInstance().revokeColor(TKK_REF);
	}
}