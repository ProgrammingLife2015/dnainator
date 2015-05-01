package nl.tudelft.dnainator.core;

/**
 * Implements a default sequence conform the Sequence interface.
 */
public class DefaultSequenceNode implements SequenceNode {
	
	private int id;
	private String source;
	private int start;
	private int end;
	private String sequence;

	/**
	 * Constructs a default sequence with all parameters specified.
	 * @param id The ID of this sequence.
	 * @param source The sources of the sequence (from where it was sequenced).
	 * @param start The start position of the sequence.
	 * @param end The end position of the sequence.
	 * @param sequence The sequence.
	 */
	public DefaultSequenceNode(int id, String source, int start, int end, String sequence) {
		this.id = id;
		this.source = source;
		this.start = start;
		this.end = end;
		this.sequence = sequence;
	}
	
	@Override
	public int getId() {
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
}
