package nl.tudelft.dnainator.parser.exceptions;

public abstract class ParseException extends Exception {
	public ParseException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = -4680051600548666152L;
}
