package nl.tudelft.dnainator.core;

/**
 * An abstract factory for generating sequences.
 */
public interface SequenceNodeFactory {

	/**
	 * Creates a new Sequence with specified references, start- and end positions.
	 * Uses the content set with {@code setContent(String content}.
	 *
	 * @param id       The ID of the {@link SequenceNode}to be built.
	 * @param refs     The references.
	 * @param startPos The starting position.
	 * @param endPos   The ending position.
	 * @param sequence The sequence data.
	 * @return The built Sequence.
	 */
	SequenceNode build(String id, String refs, int startPos, int endPos,
			String sequence);
}
