package nl.tudelft.dnainator.javafx.exceptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test the {@link AllColorsInUseException}.
 * This exception is thrown whenever all colors from the color palette are in use,
 * and another is requested.
 */
public class AllColorsInUseExceptionTest {
	
	/**
	 * Test what happens when all colors are in use.
	 * @throws AllColorsInUseException	when no colors are free.
	 */
	@Test(expected = AllColorsInUseException.class)
	public void testAllColorsInUseException() throws AllColorsInUseException {
		AllColorsInUseException colorsInUse = new AllColorsInUseException();
		assertEquals("All colors are currently in use. To highlight another strain, "
				+ "please un-highlight another strain first.", colorsInUse.getMessage());
		throw colorsInUse;
	}
}
