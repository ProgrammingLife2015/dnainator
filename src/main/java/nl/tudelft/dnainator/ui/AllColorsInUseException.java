package nl.tudelft.dnainator.ui;

/**
 * An exception to indicate to the user that all colors are currently in use.
 */
public class AllColorsInUseException extends Exception {

	/**
	 * Constructs a new {@link AllColorsInUseException}.
	 */
	public AllColorsInUseException() {
		super("All colors are currently in use. To highlight another strain, "
				+ "please un-highlight another strain first.");
	}
}
