package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceGraph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * An interface for parsing a file input stream containing edge entries.
 */
public interface EdgeParser {

	/**
	 * Connects the nodes given according to the edges read from the
	 * given {@link InputStream}, and returns a {@link SequenceGraph}.
	 * @param nodes  The nodes which should be connected.
	 * @param is The {@link InputStream} describing the edges.
	 * @return a complete SequenceGraph instance.
	 */
	default SequenceGraph parse(Map<String, Sequence> nodes, InputStream is) {
		return parse(nodes, new BufferedReader(new InputStreamReader(is)));
	}

	/**
	 * Connects the nodes given according to the edges read from the
	 * given {@link BufferedReader}, and returns a {@link SequenceGraph}.
	 * @param nodes  The nodes which should be connected.
	 * @param br The {@link BufferedReader} describing the edges.
	 * @return a complete SequenceGraph instance.
	 */
	SequenceGraph parse(Map<String, Sequence> nodes, BufferedReader br);
}
