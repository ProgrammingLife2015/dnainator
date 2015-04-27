package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceGraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * An implementation for parsing an edge file input stream.
 */
public class DefaultEdgeParser implements EdgeParser {

	@Override
	public SequenceGraph parse(Map<String, Sequence> nodes, FileInputStream edgeIn)
			throws IOException {
		return null;
	}
}
