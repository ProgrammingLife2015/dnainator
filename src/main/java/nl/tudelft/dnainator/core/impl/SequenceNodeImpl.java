package nl.tudelft.dnainator.core.impl;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;

/**
 * Implements a default sequence conform the Sequence interface.
 */
public class SequenceNodeImpl implements SequenceNode {
	private String id;
	private String source;
	private int start;
	private int end;
	private String sequence;
	private int rank;
	private List<String> outgoing;

	/**
	 * Constructs a default sequence with all parameters specified.
	 * @param id The ID of this sequence.
	 * @param source The sources of the sequence (from where it was sequenced).
	 * @param start The start position of the sequence.
	 * @param end The end position of the sequence.
	 * @param sequence The sequence.
	 */
	public SequenceNodeImpl(String id, String source, int start, int end, String sequence) {
		this(id, source, start, end, sequence, 0, new ArrayList<>());
	}

	/**
	 * Constructs a default sequence with all parameters specified.
	 * @param id The ID of this sequence.
	 * @param source The sources of the sequence (from where it was sequenced).
	 * @param start The start position of the sequence.
	 * @param end The end position of the sequence.
	 * @param sequence The sequence.
	 * @param rank The rank.
	 * @param outgoing The neighbours
	 */
	public SequenceNodeImpl(String id, String source,
			int start, int end, String sequence, int rank, List<String> outgoing) {
		this.id = id;
		this.source = source;
		this.start = start;
		this.end = end;
		this.sequence = sequence;
		this.rank = rank;
		this.outgoing = outgoing;
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
	public boolean equals(Object obj) {
		if (!(obj instanceof SequenceNode)) {
			return false;
		}

		SequenceNode other = (SequenceNode) obj;
		return id.equals(other.getId()) && source.equals(other.getSource())
				&& start == other.getStartRef() && end == other.getEndRef()
				&& sequence.equals(other.getSequence());
	}

	@Override
	public int hashCode() {
		return id.hashCode() + source.hashCode() + sequence.hashCode()
				+ Integer.hashCode(start) + Integer.hashCode(end);
	}

	@Override
	public String toString() {
		return "SequenceNode<" + getId() + "," + sequence.length() + ">";
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public List<String> getOutgoing() {
		return outgoing;
	}
}
