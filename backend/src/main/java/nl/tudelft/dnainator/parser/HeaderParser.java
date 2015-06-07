package nl.tudelft.dnainator.parser;

import nl.tudelft.dnainator.parser.exceptions.InvalidHeaderFormatException;

/**
 * Parses the header of a node.
 */
public class HeaderParser {
	private static final char DELIM = '|';
	private static final int MAX_TOKENS = 4;
	private String header;
	private int count;
	private int nextIndex;

	/**
	 * Create a new {@link HeaderParser} which will parse the supplied {@link String}.
	 * @param header The header to parse.
	 */
	public HeaderParser(String header) {
		this.header = header;
		count = 0;
		nextIndex = header.indexOf(DELIM);
	}

	/**
	 * Returns the next token, delimited by DELIM.
	 * @return The token, as {@link String}.
	 * @throws InvalidHeaderFormatException if the header has too little or too many tokens.
	 */
	public String next() throws InvalidHeaderFormatException {
		if (count >= MAX_TOKENS) {
			throw new InvalidHeaderFormatException("Leftover string: " + header);
		}
		count++;
		if (nextIndex < 0) {
			if (count < MAX_TOKENS) {
				throw new InvalidHeaderFormatException("Not enough tokens, wanted " + MAX_TOKENS
						+ ", got " + count);
			}
			return header.trim();
		}
		String next = header.substring(0, nextIndex).trim();
		header = header.substring(nextIndex + 1);
		nextIndex = header.indexOf(DELIM);
		return next;
	}

}
