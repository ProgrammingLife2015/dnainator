package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.annotation.impl.DRMutation;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * An interface for parsing drug resistant mutations entries, using an Iterator pattern.
 */
public interface DRMutationParser {
	/**
	 * Whether there's a next mutation that can be parsed.
	 * @return true if there's a next mutation, false otherwise.
	 * @throws IOException when an error occurs while reading.
	 */
	boolean hasNext() throws IOException;

	/**
	 * Returns the next {@link DRMutation}, or throws a {@link NoSuchElementException}
	 * if there's nothing left to be parsed.
	 * @return A parsed {@link DRMutation}.
	 * @throws IOException when an error occurs while reading.
	 */
	DRMutation next() throws IOException;

	/**
	 * Tries to close the {@link java.io.BufferedReader}.
	 * @throws IOException when the reader fails to close.
	 */
	void close() throws IOException;
}
