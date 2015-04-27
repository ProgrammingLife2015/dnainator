package nl.tudelft.dnainator.parser;

/**
 * A customized exception that is thrown when a FASTA file contains
 * an incorrectly formatted header section.
 */
public class InvalidHeaderFormatException extends Exception {

    /**
     * Constructs the exception with the provided message.
     *
     * @param msg The message to display.
     */
    public InvalidHeaderFormatException(String msg) {
        super(msg);
    }
}
