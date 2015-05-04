package nl.tudelft.dnainator.parser.buffered;

import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * An implementation for parsing an edge file input stream.
 */
public class DefaultEdgeParser extends BufferedEdgeParser {
	private Edge<String> current;
	private boolean needParse = true; // Whether we have to parse a new line or not.
	private static final int ID_LENGTH_GUESS = 8;
	private static final Set<Character> WHITESPACE = new HashSet<>();

	static {
		Collections.addAll(WHITESPACE, '\n', '\r', '\t', ' ');
	}

	/**
	 * Constructs a {@link DefaultEdgeParser}, which reads from
	 * the given {@link BufferedReader}.
	 *
	 * @param br The {@link BufferedReader} to read from.
	 */
	public DefaultEdgeParser(BufferedReader br) {
		super(br);
		current = null;
	}

	@Override
	public boolean hasNext() throws IOException, InvalidEdgeFormatException {
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
	private Edge<String> parse() throws IOException, InvalidEdgeFormatException {
		int first = eatWhitespace(br.read());
		if (first == -1) {
			return null;
		}
		return new Edge<>(parseSource(first), parseDest());
	}

	/*
	 * Parses the source part of the input line.
	 *
	 * @param first The first character of the input line. Always >= 0.
	 * @return The source id, as an int.
	 * @throws IOException Thrown when the reader fails.
	 */
	private String parseSource(int first) throws IOException, InvalidEdgeFormatException {
		StringBuilder source = new StringBuilder(ID_LENGTH_GUESS);
		char next = (char) eatSpaces(first);

		do {
			source.append(next);
			next = (char) br.read();
		} while (next != ' ');

		return source.toString();
	}

	/**
	 * Eats spaces until a non-space character is found.
	 * @param next the next character to test for space.
	 * @return the non-space character (or -1 if end is reached).
	 * @throws IOException if something went wrong I/O-wise.
	 */
	private int eatSpaces(int next) throws IOException {
		while ((char) next == ' ') {
			next = br.read();
		}
		return next;
	}

	private int eatWhitespace(int next) throws IOException {
		while (WHITESPACE.contains((char) next)) {
			next = br.read();
		}
		return next;
	}

	/*
	 * Parses the destination part of the input line.
	 *
	 * @return The destination id, as an int.
	 * @throws IOException Thrown when the BufferedReader fails.
	 */
	private String parseDest() throws IOException, InvalidEdgeFormatException {
		StringBuilder dest = new StringBuilder(ID_LENGTH_GUESS);
		int point = eatSpaces(br.read());
		boolean destParsed = false;
		char next;

		parseLoop: while (point != -1) {
			next = (char) point;
			switch (next) {
			case '\n': case '\r':
				break parseLoop;
			case ' ':
				destParsed = true;
				point = eatSpaces(br.read());
				break;
			default:
				if (destParsed) {
					throw new InvalidEdgeFormatException("Found extra node");
				}
				dest.append(next);
				point = br.read();
			}
		}

		if (dest.length() == 0) {
			throw new InvalidEdgeFormatException("Missing destination node");
		}
		return dest.toString();
	}

	@Override
	public Edge<String> next() throws IOException, InvalidEdgeFormatException {
		if (hasNext()) {
			needParse = true;
			return current;
		}
		throw new NoSuchElementException();
	}

}
