package nl.tudelft.dnainator.parser.buffered;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;
import nl.tudelft.dnainator.parser.InvalidHeaderFormatException;
import nl.tudelft.dnainator.parser.NodeParser;

/**
 * A variant of the {@link NodeParser}, which uses a {@link BufferedReader}.
 */
public abstract class BufferedNodeParser extends NodeParser {

	/**
	 * Creates the parser with the provided SequenceFactory.
	 *
	 * @param sf The SequenceFactory to be used.
	 */
	public BufferedNodeParser(SequenceFactory sf) {
		super(sf);
	}

	@Override
	public Map<String, Sequence> parse(InputStream is)
			throws NumberFormatException, InvalidHeaderFormatException, IOException {
		return parse(new BufferedReader(new InputStreamReader(is)));
	}

	/**
	 * Tries to parse the given input stream to a {@link Map} in which the tuples denote the
	 * header and data of the nodes.
	 * The caller is responsible for closing the input stream.
	 * @param br The {@link BufferedReader} to be used for parsing.
	 * @return A {@link Map} of IDs to {@link Sequence}s containing all nodes.
	 * @throws NumberFormatException        Thrown when the input contains a NaN where it should
	 *                                      not.
	 * @throws InvalidHeaderFormatException Thrown when a FASTA header section is invalid.
	 * @throws IOException                  Thrown when reading the FileInputStream fails.
	 */
	public abstract Map<String, Sequence> parse(BufferedReader br)
			throws NumberFormatException, InvalidHeaderFormatException, IOException;

}
