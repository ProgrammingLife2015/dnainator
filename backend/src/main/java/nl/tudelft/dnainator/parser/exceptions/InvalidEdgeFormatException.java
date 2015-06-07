package nl.tudelft.dnainator.parser.exceptions;

/**
 * An {@link Exception} used to report an error during the parsing of the edges file if
 * a line is found with unexpected data.
 */
public class InvalidEdgeFormatException extends ParseException {
	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -4475355560535116525L;

	/**
	 * Constructs the exception with the provided message.
	 *
	 * @param msg The message to display.
	 */
	public InvalidEdgeFormatException(String msg) {
		super(msg);
	}
}
