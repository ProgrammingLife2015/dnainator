package nl.tudelft.dnainator.core;

/**
 * An abstract factory for generating sequences.
 */
public interface SequenceFactory {

	/**
	 * Sets the content (i.e. DNA) for the Sequence to use.
	 *
	 * @param content the new content type.
	 */
	void setContent(String content);

	/**
	 * Creates a new Sequence with specified references, start- and end positions.
	 * Uses the content set with {@code setContent(String content}.
	 *
	 * @param refs     The references.
	 * @param startPos The starting position.
	 * @param endPos   The ending position.
	 * @return The built Sequence.
	 */
	Sequence build(String refs, int startPos, int endPos);
}
