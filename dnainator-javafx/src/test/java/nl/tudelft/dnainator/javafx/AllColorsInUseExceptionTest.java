package nl.tudelft.dnainator.javafx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AllColorsInUseExceptionTest {

	@Test(expected=AllColorsInUseException.class)
	public void testAllColorsInUseException() throws AllColorsInUseException {
		AllColorsInUseException colorsInUse = new AllColorsInUseException();
		assertEquals("All colors are currently in use. To highlight another strain, "
				+ "please un-highlight another strain first.", colorsInUse.getMessage());
		throw colorsInUse;
	}
}
