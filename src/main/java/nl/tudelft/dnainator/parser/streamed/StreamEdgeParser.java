package nl.tudelft.dnainator.parser.streamed;

import java.io.InputStream;

import nl.tudelft.dnainator.parser.EdgeParser;


public abstract class StreamEdgeParser implements EdgeParser {
	protected InputStream is;

	public StreamEdgeParser(InputStream is) {
		this.is = is;
	}

}
