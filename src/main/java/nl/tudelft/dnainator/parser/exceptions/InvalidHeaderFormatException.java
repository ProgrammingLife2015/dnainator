package nl.tudelft.dnainator.parser.exceptions;

/**
 * A customized exception that is thrown when a FASTA file contains
 * an incorrectly formatted header section.
 */
public class InvalidHeaderFormatException extends ParseException {
	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -8296140307732116580L;

	/**
	 * Constructs the exception with the provided message.
	 *
	 * @param msg The message to display.
	 */
	public InvalidHeaderFormatException(String msg) {
		super(msg);
	}
}
