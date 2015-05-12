package nl.tudelft.dnainator.parser;

import java.io.BufferedReader;
import java.io.IOException;

import nl.tudelft.dnainator.core.SequenceNodeFactory;

/**
 * A {@link NodeParser}, which uses a {@link BufferedReader} as its source.
 */
public abstract class BufferedNodeParser implements NodeParser {

	protected BufferedReader br;
	protected SequenceNodeFactory sf;

	/**
	 * Creates the parser with the provided {@link SequenceNodeFactory}.
	 *
	 * @param sf The {@link SequenceNodeFactory} to be used.
	 * @param br The {@link BufferedReader} from which to read.
	 */
	public BufferedNodeParser(SequenceNodeFactory sf, BufferedReader br) {
		this.sf = sf;
		this.br = br;
	}

	@Override
	public void close() throws IOException {
		br.close();
	}

}
