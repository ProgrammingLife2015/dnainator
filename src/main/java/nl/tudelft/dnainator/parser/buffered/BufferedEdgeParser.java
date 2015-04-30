package nl.tudelft.dnainator.parser.buffered;

import java.io.BufferedReader;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.streamed.StreamEdgeParser;

/**
 * A variant of {@link StreamEdgeParser}, which uses a {@link BufferedReader}.
 */
public abstract class BufferedEdgeParser implements EdgeParser {
	protected BufferedReader br;

	/**
	 * Construct a new {@link BufferedEdgeParser}.
	 * @param br The {@link BufferedReader} to read from.
	 */
	public BufferedEdgeParser(BufferedReader br) {
		this.br = br;
	}

}