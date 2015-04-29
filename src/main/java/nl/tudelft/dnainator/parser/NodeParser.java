package nl.tudelft.dnainator.parser;

import java.io.IOException;

import nl.tudelft.dnainator.core.SequenceNode;

/**
 * An interface for parsing nodes, using the Iterator pattern.
 */
public interface NodeParser {

	/**
	 * Whether there's a next node to be parsed.
	 * @return true if there's a next node.
	 * @throws IOException if something went wrong while reading.
	 */
	boolean hasNext() throws IOException;

	/**
	 * Get the next parsed {@link SequenceNode}.
	 * @return the {@link SequenceNode} parsed.
	 * @throws IOException if something went wrong while reading.
	 * @throws InvalidHeaderFormatException if the header of the node is invalid.
	 */
	SequenceNode next() throws IOException, InvalidHeaderFormatException;

}
