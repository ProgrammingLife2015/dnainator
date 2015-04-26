package nl.tudelft.dnainator.parser;

import static java.lang.Integer.parseInt;
import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

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
	 * Fills a {@link Sequence} using the given {@link SequenceFactory} with the parsed
	 * header.
	 * @param sf The {@link SequenceFactory} used to create a {@link Sequence}.
	 * @return The sequence containing the header information.
	 * @throws NumberFormatException if the start and/or end position of the sequence can't
	 * be parsed.
	 * @throws InvalidHeaderFormatException if the header has too little or too many tokens.
	 */
	public Sequence fill(SequenceFactory sf) throws NumberFormatException,
													InvalidHeaderFormatException {
		return sf.build(next(), parseInt(next()), parseInt(next()));
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
