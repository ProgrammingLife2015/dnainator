package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * An interface for parsing edge entries, using the Iterator pattern.
 */
public interface EdgeParser {

	/**
	 * Whether there's a next edge that can be parsed.
	 * @return true if there's a next edge, false otherwise.
	 * @throws IOException when an error occurs while reading.
	 * @throws InvalidEdgeFormatException when an invalid line is found during reading. This
	 * can happen when, for example, the source or destination is not a number, or when the
	 * destination is missing.
	 */
	boolean hasNext() throws IOException, InvalidEdgeFormatException;

	/**
	 * Returns the next {@link Edge}, or throws a {@link NoSuchElementException}
	 * if there's nothing left to be parsed.
	 * @return A parsed {@link Edge}.
	 * @throws IOException when an error occurs while reading.
	 * @throws InvalidEdgeFormatException when an invalid line is found during reading. This
	 * can happen when, for example, the source or destination is not a number, or when the
	 * destination is missing.
	 */
	Edge<String> next() throws IOException, InvalidEdgeFormatException;

	/**
	 * Tries to close the {@link java.io.BufferedReader}.
	 * @throws IOException when the reader fails to close.
	 */
	void close() throws IOException;

}
