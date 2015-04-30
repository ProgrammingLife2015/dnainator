package nl.tudelft.dnainator.parser.buffered;

import java.io.BufferedReader;

import nl.tudelft.dnainator.core.SequenceFactory;
import nl.tudelft.dnainator.parser.NodeParser;

/**
 * A {@link NodeParser}, which uses a {@link BufferedReader} as its source.
 */
public abstract class BufferedNodeParser implements NodeParser {

	protected BufferedReader br;
	protected SequenceFactory sf;

	/**
	 * Creates the parser with the provided {@link SequenceFactory}.
	 *
	 * @param sf The {@link SequenceFactory} to be used.
	 * @param br The {@link BufferedReader} from which to read.
	 */
	public BufferedNodeParser(SequenceFactory sf, BufferedReader br) {
		this.sf = sf;
		this.br = br;
	}

}
