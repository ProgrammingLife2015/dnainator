package nl.tudelft.dnainator.parser.buffered;

import java.io.BufferedReader;
import nl.tudelft.dnainator.parser.EdgeParser;

/**
 * An {@link EdgeParser} which uses a {@link BufferedReader} as its source.
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