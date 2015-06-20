package nl.tudelft.dnainator.parser;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A generic {@link Parser} which uses a {@link BufferedReader} as its source.
 * @param <T> the type this parser returns
 */
public abstract class BufferedParser <T> implements Parser<T> {
	protected BufferedReader br;

	/**
	 * Construct a new {@link BufferedParser}.
	 * @param br The {@link BufferedReader} to read from.
	 */
	public BufferedParser(BufferedReader br) {
		this.br = br;
	}

	@Override
	public void close() throws IOException {
		br.close();
	}

}