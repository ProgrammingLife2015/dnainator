package nl.tudelft.dnainator.parser;

import java.io.IOException;

import nl.tudelft.dnainator.core.Sequence;

public interface NodeParser {

	boolean hasNext() throws IOException;

	Sequence next() throws IOException, InvalidHeaderFormatException;

}
