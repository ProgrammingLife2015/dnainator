package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.SequenceNode;

import java.io.IOException;

/**
 * An interface for parsing nodes, using the Iterator pattern.
 * @param <T> the type this parser returns
 */
public interface Parser<T> {

	/**
	 * Whether there's a next node to be parsed.
	 * @return true if there's a next node.
	 * @throws IOException if something went wrong while reading.
	 */
	boolean hasNext() throws IOException;

	/**
	 * Get the next parsed item.
	 * @return the parsed item.
	 * @throws IOException if something went wrong while reading.
	 */
	T next() throws IOException;

	/**
	 * Tries to close the {@link java.io.BufferedReader}.
	 * @throws IOException when the reader fails to close.
	 */
	void close() throws IOException;
}
