package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.util.Edge;

import java.io.IOException;

/**
 * An interface for parsing edge entries, using the Iterator pattern.
 */
public interface EdgeParser {

	/**
	 * Whether there's a next edge that can be parsed.
	 * @return true if there's a next edge, false otherwise.
	 * @throws IOException when an error occurs while reading.
	 */
	boolean hasNext() throws IOException;

	/**
	 * Returns the next {@link Edge}, or throws a {@link NoSuchElementException}
	 * if there's nothing left to be parsed.
	 * @return A parsed {@link Edge}.
	 * @throws IOException when an error occurs while reading.
	 */
	Edge<Integer> next() throws IOException;

}
