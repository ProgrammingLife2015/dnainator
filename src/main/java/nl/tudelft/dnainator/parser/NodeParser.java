package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * An abstract implementation for parsing node file input streams.
 */
public abstract class NodeParser {
	protected SequenceFactory sf;

	/**
	 * Creates the parser with the provided SequenceFactory.
	 *
	 * @param sf The SequenceFactory to be used.
	 */
	public NodeParser(SequenceFactory sf) {
		this.sf = sf;
	}

	/**
	 * Same as {@link #parse(BufferedReader)}, only using a {@link InputStream}.
	 * @param is The {@link InputStream} to be used for parsing.
	 * @return A {@link Map} of IDs to {@link Sequence}s containing all nodes.
	 * @throws NumberFormatException        Thrown when the input contains a NaN where it should
	 *                                      not.
	 * @throws InvalidHeaderFormatException Thrown when a FASTA header section is invalid.
	 * @throws IOException                  Thrown when reading the FileInputStream fails.
	 */
	public abstract Map<String, ? extends Sequence> parse(InputStream is)
			throws NumberFormatException, InvalidHeaderFormatException, IOException;
}
