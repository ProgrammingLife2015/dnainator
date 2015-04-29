package nl.tudelft.dnainator.parser.streamed;

import java.io.InputStream;

import nl.tudelft.dnainator.parser.EdgeParser;

/**
 * An {@link EdgeParser} using an {@link InputStream} as its source.
 */
public abstract class StreamEdgeParser implements EdgeParser {
	protected InputStream is;

	/**
	 * Creates a new {@link StreamEdgeParser}, which will read from the given
	 * {@link InputStream}.
	 * @param is The {@link InputStream} to read the nodes from.
	 */
	public StreamEdgeParser(InputStream is) {
		this.is = is;
	}

}
