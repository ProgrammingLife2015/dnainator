package nl.tudelft.dnainator.parser;

import java.io.File;
import java.util.Map;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceGraph;

public interface EdgeParser {

	/**
	 * Connects the nodes given according to the edge
	 * file.
	 * @param nodes The nodes which should be connected.
	 * @param edgeFile The file describing the edges.
	 * @return
	 */
	public SequenceGraph parse(Map<String, Sequence> nodes, File edgeFile);

}
