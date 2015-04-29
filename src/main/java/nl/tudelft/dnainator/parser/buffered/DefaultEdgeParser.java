package nl.tudelft.dnainator.parser.buffered;

import nl.tudelft.dnainator.util.Edge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * An implementation for parsing an edge file input stream.
 */
public class DefaultEdgeParser extends BufferedEdgeParser {
	private Edge<Integer> current;
	private boolean needParse = true; // Whether we have to parse a new line or not.
	private static final int ID_LENGTH_GUESS = 8;

	/**
	 * Constructs a {@link DefaultEdgeParser}, which reads from
	 * the given {@link BufferedReader}.
	 * @param br The {@link BufferedReader} to read from.
	 */
	public DefaultEdgeParser(BufferedReader br) {
		super(br);
		current = null;
	}

	@Override
	public boolean hasNext() throws IOException {
		if (needParse) {
			current = parse();
			needParse = false;
		}
		return current != null;
	}

	/*
	 * Creates an Edge from the next line of input. Reads the first
	 * character and checks if it is valid.
	 *
	 * @return The parsed edge, containing a source and a destination or null if
	 * the first character marked the end of the file.
	 * @throws IOException Thrown when the reader fails.
	 */
	private Edge<Integer>parse() throws IOException {
		int first = br.read();
		if (first == -1) {
			return null;
		}
		return new Edge<>(parseSource((char) first), parseDest());
	}

	/*
	 * Parses the source part of the input line.
	 *
	 * @param first The first character of the input line. Always >= 0.
	 * @return The source id, as an int.
	 * @throws IOException Thrown when the reader fails.
	 */
	private int parseSource(char first) throws IOException {
		StringBuilder source = new StringBuilder(ID_LENGTH_GUESS);
		char next = first;

		do {
			source.append(next);
			next = (char) br.read();
		} while (next != ' ');

		return Integer.parseInt(source.toString());
	}

	/*
	 * Parses the destination part of the input line.
	 *
	 * @return The destination id, as an int.
	 * @throws IOException Thrown when the BufferedReader fails.
	 */
	private int parseDest() throws IOException {
		StringBuilder dest = new StringBuilder(ID_LENGTH_GUESS);
		int point = br.read();
		char next;

		while (point != -1) {
			next = (char) point;
			if (next == '\n' || next == '\r') {
				return Integer.parseInt(dest.toString());
			}
			dest.append(next);
			point = br.read();
		}

		return Integer.parseInt(dest.toString());
	}

	@Override
	public Edge<Integer> next() throws IOException {
		if (hasNext()) {
			needParse = true;
			return current;
		}
		throw new NoSuchElementException();
	}

}
