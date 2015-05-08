package nl.tudelft.dnainator.core;

/**
 * Implements a default sequence conform the Sequence interface.
 */
public class DefaultSequenceNode implements SequenceNode {

	private String id;
	private String source;
	private int start;
	private int end;
	private String sequence;
	private int rank;

	/**
	 * Constructs a default sequence with all parameters specified.
	 * @param id The ID of this sequence.
	 * @param source The sources of the sequence (from where it was sequenced).
	 * @param start The start position of the sequence.
	 * @param end The end position of the sequence.
	 * @param sequence The sequence.
	 */
	public DefaultSequenceNode(String id, String source, int start, int end, String sequence) {
		this(id, source, start, end, sequence, 0);
	}

	/**
	 * Constructs a default sequence with all parameters specified.
	 * @param id The ID of this sequence.
	 * @param source The sources of the sequence (from where it was sequenced).
	 * @param start The start position of the sequence.
	 * @param end The end position of the sequence.
	 * @param sequence The sequence.
	 * @param rank The rank.
	 */
	public DefaultSequenceNode(String id, String source,
			int start, int end, String sequence, int rank) {
		this.id = id;
		this.source = source;
		this.start = start;
		this.end = end;
		this.sequence = sequence;
		this.rank = rank;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public int getStartRef() {
		return start;
	}

	@Override
	public int getEndRef() {
		return end;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	@Override
	public String toString() {
		return "SequenceNode<" + getId() + "," + getStartRef() + "," + getEndRef()
				+ "," + getSource() + "," + getSequence() + ">";
	}

	@Override
	public int getRank() {
		return rank;
	}
}
