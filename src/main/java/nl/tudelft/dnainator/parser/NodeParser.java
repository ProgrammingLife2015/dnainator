package nl.tudelft.dnainator.parser;

import java.io.IOException;

import nl.tudelft.dnainator.core.SequenceNode;

public interface NodeParser {

	boolean hasNext() throws IOException;

	SequenceNode next() throws IOException, InvalidHeaderFormatException;

}
