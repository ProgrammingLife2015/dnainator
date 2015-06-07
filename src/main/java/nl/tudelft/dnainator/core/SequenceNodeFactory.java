package nl.tudelft.dnainator.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An abstract factory for generating sequences.
 */
public interface SequenceNodeFactory {

	/**
	 * Creates a new Sequence with specified references, start- and end positions.
	 * Uses the content set with {@code setContent(String content}.
	 *
	 * @param id       The ID of the {@link SequenceNode}to be built.
	 * @param refs     The references, in a comma-separated string.
	 * @param startPos The starting position.
	 * @param endPos   The ending position.
	 * @param sequence The sequence data.
	 * @return The built Sequence.
	 */
	SequenceNode build(String id, String refs, int startPos, int endPos,
			String sequence);

	/**
	 * Creates a new Sequence with specified references, start- and end positions.
	 * Uses the content set with {@code setContent(String content}.
	 *
	 * @param id       The ID of the {@link SequenceNode}to be built.
	 * @param refs     The references, as a list.
	 * @param startPos The starting position.
	 * @param endPos   The ending position.
	 * @param sequence The sequence data.
	 * @return The built Sequence.
	 */
	default SequenceNode build(String id, List<String> refs, int startPos, int endPos,
			String sequence) {
		return build(id, new HashSet<>(refs), startPos, endPos, sequence);
	}

	/**
	 * Creates a new Sequence with specified references, start- and end positions.
	 * Uses the content set with {@code setContent(String content}.
	 *
	 * @param id       The ID of the {@link SequenceNode}to be built.
	 * @param refs     The references, as unique set.
	 * @param startPos The starting position.
	 * @param endPos   The ending position.
	 * @param sequence The sequence data.
	 * @return The built Sequence.
	 */
	SequenceNode build(String id, Set<String> refs, int startPos, int endPos,
			String sequence);
}
