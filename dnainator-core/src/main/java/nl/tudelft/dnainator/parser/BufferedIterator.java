package nl.tudelft.dnainator.parser;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A generic {@link Iterator} which uses a {@link BufferedReader} as its source.
 * @param <T> the type this parser returns
 */
public abstract class BufferedIterator <T> implements Iterator<T> {
	protected BufferedReader br;

	/**
	 * Constructs a {@link BufferedIterator}, which reads from the given {@link BufferedReader}.
	 * @param br The {@link BufferedReader} to read from.
	 */
	public BufferedIterator(BufferedReader br) {
		this.br = br;
	}

	@Override
	public void close() throws IOException {
		br.close();
	}

}