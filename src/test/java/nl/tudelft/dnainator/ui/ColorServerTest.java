package nl.tudelft.dnainator.ui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testset for the {@link ColorServer}.
 */
public class ColorServerTest {
	private static final String TKK_REF = "TKK_REF";
	private static final String TKK_REF_0026 = "TKK_REF_0026";
	private ColorServer colorServer;

	/**
	 * Sets up the test environment.
	 */
	@Before
	public void setup() {
		colorServer = new ColorServer();
	}

	/**
	 * Tests whether simply getting one color that is not used yet, works.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorSimple() throws AllColorsInUseException {
		colorServer.askColor(TKK_REF);
		String col0 = colorServer.getColor(TKK_REF);
		assertEquals("color-0", col0);
		colorServer.revokeColor(TKK_REF);
	}

	/**
	 * Tests if the {@link ColorServer} correctly increments the count.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorMultiple() throws AllColorsInUseException {
		colorServer.askColor(TKK_REF);
		String col0 = colorServer.getColor(TKK_REF);
		assertEquals("color-0", col0);
		colorServer.askColor(TKK_REF_0026);
		String col1 = colorServer.getColor(TKK_REF_0026);
		assertEquals("color-1", col1);
		colorServer.revokeColor(TKK_REF);
		colorServer.revokeColor(TKK_REF_0026);
	}

	/**
	 * Tests if retrieving a color for the same strain retrieves the same {@link String}.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testGetColorSame() throws AllColorsInUseException {
		colorServer.askColor(TKK_REF);
		String col0 = colorServer.getColor(TKK_REF);
		String col1 = colorServer.getColor(TKK_REF);
		assertTrue(col0 == col1);
		colorServer.revokeColor(TKK_REF);
	}

	/**
	 * Tests if revoking a color works properly.
	 * @throws AllColorsInUseException Won't be thrown.
	 */
	@Test
	public void testRevoke() throws AllColorsInUseException {
		colorServer.askColor(TKK_REF);
		String col0 = colorServer.getColor(TKK_REF);
		assertEquals("color-0", col0);
		colorServer.revokeColor(TKK_REF);
		colorServer.askColor(TKK_REF);
		String col1 = colorServer.getColor(TKK_REF);
		assertEquals("color-0", col1);
		assertTrue(col0 == col1);
		colorServer.revokeColor(TKK_REF);
	}
}
