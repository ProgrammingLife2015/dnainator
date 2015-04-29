package nl.tudelft.dnainator.parser.streamed;

import nl.tudelft.dnainator.core.SequenceFactory;
import nl.tudelft.dnainator.parser.NodeParser;

import java.io.InputStream;

/**
 * An abstract implementation for parsing node {@link InputStream}s.
 */
public abstract class StreamNodeParser implements NodeParser {
	protected SequenceFactory sf;
	protected InputStream is;

	/**
	 * Creates the parser with the provided {@link SequenceFactory}.
	 *
	 * @param sf The {@link SequenceFactory} to be used.
	 * @param is The {@link InputStream} used.
	 */
	public StreamNodeParser(SequenceFactory sf, InputStream is) {
		this.sf = sf;
		this.is = is;
	}

}
