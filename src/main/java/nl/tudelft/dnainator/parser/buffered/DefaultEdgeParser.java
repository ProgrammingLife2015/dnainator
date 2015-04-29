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
	private boolean depleted = false;
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

	private Edge<Integer>parse() throws IOException {
		int point = br.read();
		depleted = point == -1;
		if (depleted) {
			return null;
		}
		StringBuilder source = new StringBuilder(ID_LENGTH_GUESS);
		StringBuilder dest = new StringBuilder(ID_LENGTH_GUESS);
		boolean sourceParsed = false;
		parseLoop: while (!depleted) {
			char toChar = (char) point;
			switch (toChar) {
			case '\n': case '\r':
				break parseLoop;
			case ' ':
				sourceParsed = true;
				break;
			default:
				if (sourceParsed) {
					dest.append(toChar);
				} else {
					source.append(toChar);
				}
			}
			point = br.read();
			depleted = point == -1;
		}
		return new Edge<Integer>(Integer.parseInt(source.toString()),
				Integer.parseInt(dest.toString()));
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
